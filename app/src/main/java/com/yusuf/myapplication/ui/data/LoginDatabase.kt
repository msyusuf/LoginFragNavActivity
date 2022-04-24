package com.yusuf.myapplication.ui.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yusuf.myapplication.ui.data.Constants.Companion.LOG_TAG
import com.yusuf.myapplication.ui.data.model.LoggedInUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * This is the backend. The database. This used to be done by the OpenHelper.
 * The fact that this has very few comments emphasizes its coolness.
 */
@Database(entities = [
    LoggedInUser::class],
    version = 1, exportSchema = false)

abstract class LoginDatabase : RoomDatabase() {

    abstract fun loginDao(): LoginDao

    companion object {
        @Volatile
        private var INSTANCE: LoginDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): LoginDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            Log.i(LOG_TAG, "Inside create database...")
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LoginDatabase::class.java,
                    "login_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
                    .addCallback(LoginDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                Log.i(LOG_TAG, "Inside create database's companion object. DB instance created...")
                // return instance
                instance
            }
        }

        private class LoginDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onCreate method to populate the database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Log.i(LOG_TAG, "Inside LoginDatabase, onCreate")

                // If you want to keep the data through app restarts,
                // comment out the following line.
                try {
                    INSTANCE?.let { database ->
                        scope.launch(Dispatchers.IO) {
                            populateDatabase(database.loginDao())
                        }
                    }
                } catch (e: Exception) {
                    Log.i(LOG_TAG, "**** Exception populating DB. ${e.message}")
                }
            }

            /**
             * Populate the database in a new coroutine.
             */

            suspend fun populateDatabase(loginDao: LoginDao) {
                // Start the app with a clean database every time.
                // Not needed if you only populate on creation.
                Log.i(
                    LOG_TAG,
                    "**** FIX ME - from callback, populateDatabase(Dao). Keeping track on how often this is called."
                )
                val user = LoggedInUser(0, "dummy", "dummyPassword", "InvalidToken")
                loginDao.insert(user)

            } // end populateDatabase
        }
    }
}
