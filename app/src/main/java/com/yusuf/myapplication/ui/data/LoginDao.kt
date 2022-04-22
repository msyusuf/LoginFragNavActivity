package com.yusuf.myapplication.ui.data

import androidx.room.*
import androidx.room.Dao
import com.yusuf.myapplication.ui.data.model.LoggedInUser
import kotlinx.coroutines.flow.Flow

@Dao
interface LoginDao {


    // Simple query that does not take parameters and returns all customers.
    @Query("SELECT * FROM loggedin_user_table")
    fun getAllUsers(): Flow<List<LoggedInUser>>

    @Query("SELECT token FROM loggedin_user_table LIMIT 1")
    suspend fun getTokenFromRoomDb(): String

//    Need a type converter to put wildcard character.
//    @Query("SELECT * FROM loggedin_user_table WHERE email LIKE :email LIMIT 1")
//    fun getLoggedInUser(email: String): Flow<LoggedInUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: LoggedInUser)

    @Query("SELECT COUNT(*) FROM loggedin_user_table")
    suspend fun getLoggedInUserCount(): Int

    // Simple query that does not take parameters and returns nothing.
    @Query("DELETE FROM loggedin_user_table")
    suspend fun deleteAllUsers()

    // May need a type converter.
    // https://clintpaul.medium.com/what-is-typeconverter-in-room-how-to-use-it-properly-e7b4847012b4
    // https://developer.android.com/training/data-storage/room/referencing-data
    // https://stackoverflow.com/questions/59377571/room-persistance-library-query-with-wildcards
    // @Query("UPDATE loggedin_user_table SET token = :token WHERE email = :email")
    // https://medium.com/@sienatime/enabling-sqlite-fts-in-room-2-1-75e17d0f0ff8 - Like %% <--- ***** ------
    // https://github.com/android/architecture-components-samples/commit/c0d72f210241ff310bba13db9b8748081d6feab9 - Migration
    // @Query("UPDATE loggedin_user_table SET token = :token WHERE email = :email LIKE \"test%\" ")
    // suspend fun updateToken(email: String, token: String)

    // Caution: It will update all records, but there should only be 1 record in the database.
    @Query("UPDATE loggedin_user_table SET token = :token")
    suspend fun updateToken(token: String)

    @Update
    suspend fun update(user: LoggedInUser)

}