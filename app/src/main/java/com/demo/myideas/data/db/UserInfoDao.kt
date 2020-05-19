package com.demo.myideas.data.db

import androidx.room.*
import com.demo.myideas.data.model.UserInfo
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface UserInfoDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewUser(idea: UserInfo): Completable

    @Query("DELETE FROM user_info")
    fun deleteUserToken(): Completable

    @Query("SELECT * FROM user_info")
    fun getUserInfo(): Observable<List<UserInfo>>

    @Update
    fun updateIdea(userInfo: UserInfo) : Completable

    @Delete
    fun deleteUsers(userInfo: UserInfo) :Completable
}