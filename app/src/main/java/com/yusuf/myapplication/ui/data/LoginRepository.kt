package com.yusuf.myapplication.ui.data

import android.util.Log
import androidx.annotation.WorkerThread
import com.yusuf.myapplication.ui.data.Constants.Companion.LOG_TAG
import com.yusuf.myapplication.ui.data.model.LoggedInUser
import kotlinx.coroutines.flow.Flow

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val loginDao: LoginDao) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    var token: String? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
        Log.i(LOG_TAG, "In Repository, Initialization...")
    }

//    fun logout() {
//        user = null
//        dataSource.logout()
//    }


    val allUsers: Flow<List<LoggedInUser>> = loginDao.getAllUsers()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getUserCount(): Int = loginDao.getLoggedInUserCount()

    // TODO Remove the try/Catches after testing.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertUser(user: LoggedInUser) {
        try {
            loginDao.insert(user)
            Log.i(LOG_TAG, "******** Inserted a user ${user} using Dao.")
        }
        catch (e: Exception ) {
            Log.e(LOG_TAG, String.format( "**** Exception ******** Inserted a user ${user} using Dao.", e.message))
        }
    }
    suspend fun getTokenFromRoomDb()
    {
        this.token = loginDao.getTokenFromRoomDb()
    }


    suspend fun login(user: LoggedInUser) {
        // handle login
        Log.i(LOG_TAG, "******** Login Repository, calling loginDao( ${user}).")
        loginDao.insert(user)
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    suspend fun deleteAllUsers() {
        try {
            loginDao.deleteAllUsers()
            Log.i(LOG_TAG, "LoginRepository")
        }
        catch (e: java.lang.Exception) {
            Log.e(LOG_TAG, String.format( "**** Exception ******** while DeleteAllUsers", e.message))
        }
    }

}