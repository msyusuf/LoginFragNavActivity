package com.yusuf.myapplication

import android.app.Application
import com.yusuf.myapplication.ui.data.LoginDatabase
import com.yusuf.myapplication.ui.data.LoginRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class LoginTestApplication : Application(){

    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { LoginDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { LoginRepository(database.loginDao()) }

}