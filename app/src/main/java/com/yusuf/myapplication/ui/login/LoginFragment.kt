package com.yusuf.myapplication.ui.login

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.ActivityNavigator
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.yusuf.myapplication.LoginTestApplication
import com.yusuf.myapplication.MainActivity
import com.yusuf.myapplication.R
import com.yusuf.myapplication.databinding.FragmentLoginBinding
import com.yusuf.myapplication.ui.data.Constants
import com.yusuf.myapplication.ui.data.Constants.Companion.LOG_TAG
import com.yusuf.myapplication.ui.data.LoginRepository

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory((activity?.application as LoginTestApplication).repository)
        //return@viewModels LoginViewModelFactory((activity?.application as LoginTestApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        Log.i(Constants.LOG_TAG, "LoginFragment.onCreateView()")
        return binding.root
    } // end onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.i(Constants.LOG_TAG, "LoginFragment onViewCreated()")

        val usernameEditText = binding.username
        val passwordEditText = binding.password
        val loginButton = binding.login
        val loadingProgressBar = binding.loading

        loginViewModel.loginFormState.observe(viewLifecycleOwner,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                loginButton.isEnabled = loginFormState.isDataValid
                loginFormState.usernameError?.let {
                    usernameEditText.error = getString(it)
                }
                loginFormState.passwordError?.let {
                    passwordEditText.error = getString(it)
                }
            })

        loginViewModel.loginResult.observe(viewLifecycleOwner,
            Observer { loginResult ->
                loginResult ?: return@Observer
                loadingProgressBar.visibility = View.GONE
                loginResult.error?.let {
                    showLoginFailed(it)
                }
                loginResult.success?.let {
                    updateUiWithUser(it)
                }
            })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChanged(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        }
        usernameEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
            false
        }

        loginButton.setOnClickListener { v:View ->
            Log.i(Constants.LOG_TAG, "In Login fragment, setOnClickListener, calling loginViewModel.login...")
            loadingProgressBar.visibility = View.VISIBLE

            loginViewModel.login(
                usernameEditText.text.toString(),
                passwordEditText.text.toString()
            )
            Log.i(Constants.LOG_TAG, "LeavingLoginButton.Click...")

            val args = Bundle()
            args.putString("userID", usernameEditText.text.toString())
            v.findNavController()
                .navigate(R.id.nav_home, args)

            loadingProgressBar.visibility = View.GONE
        }


    } // end onViewCreated

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome) + model.displayName
        // TODO : initiate successful logged in experience
        Log.i(Constants.LOG_TAG, "LoginFragment.updateUiWithUser(), ${welcome}")
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    private fun checkUserCount(){
        val userCount = loginViewModel.getUserCount()
        Log.i(LOG_TAG, "User count is: $userCount")
        if (userCount > 0) {
            val args = Bundle()
            args.putString("userID", "String")
            this.findNavController()
                .navigate(R.id.nav_home, args)
        } else {
            Log.i(LOG_TAG, "User count is: $userCount")
            this.findNavController()
                .navigate(R.id.nav_login)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(LOG_TAG, "**LoginFragment.onCreate()...")
        _binding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(LOG_TAG, "**LoginFragment.onDestroyView()...")
        _binding = null
    }
    override fun onStart() {
        super.onStart()
        Log.i(LOG_TAG, "**LoginFragment.onStart()...")
        _binding = null
    }
    override fun onResume() {
        super.onResume()
        Log.i(LOG_TAG, "**LoginFragment.onResume()...")
        _binding = null
    }
    override fun onDetach() {
        super.onDetach()
        Log.i(LOG_TAG, "**LoginFragment.onDetach()...")
    }
}