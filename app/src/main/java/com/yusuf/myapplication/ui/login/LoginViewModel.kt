package com.yusuf.myapplication.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.yusuf.myapplication.R
import com.yusuf.myapplication.ui.data.Constants.Companion.LOG_TAG
import com.yusuf.myapplication.ui.data.LoginRepository
import com.yusuf.myapplication.ui.data.Result
import com.yusuf.myapplication.ui.data.model.LoggedInUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        Log.i(LOG_TAG, "In LoginViewModel, calling login to insert user in LoginRepository. ")
        viewModelScope.launch {
            val user: LoggedInUser = LoggedInUser(0, username, password, "NoToken")
            loginRepository.login(user)
        }

    }

    fun loginDataChanged(username: String, password: String) {

        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
//            Log.i(LOG_TAG,
//                "In LoginViewModel,loginDataChanged(), Username: ${username} is not valid.")
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
//            Log.i(LOG_TAG,
//                "In LoginViewModel,loginDataChanged(), Password: ${password} is not valid.")
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
//            Log.i(LOG_TAG,
//                "In LoginViewModel,loginDataChanged(), username and password are valid. Username: ${username} Password: ${password}.")
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    fun getUserCount(): Int {
        var count: Int = 0
        viewModelScope.launch {
            count = loginRepository.getUserCount()
        }
        Log.i(LOG_TAG, "In LoginViewModel.getUserCount(): count = $count")
        return count
    }
}