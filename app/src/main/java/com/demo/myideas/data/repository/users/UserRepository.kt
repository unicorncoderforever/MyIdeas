package com.demo.myideas.data.repository.users

import androidx.lifecycle.LiveData
import com.demo.myideas.data.model.UserInfo
import com.demo.myideas.data.network.ApiError
import io.reactivex.disposables.Disposable


interface UserRepository {

    fun loginUser(userEmail : String ,userPassword: String,success: (UserInfo?) ->Unit,
                  failure: (ApiError)->Unit,
                  terminate: () -> Unit):Disposable

    fun getMyInfo(token : String,refeshToken : String,  success: (UserInfo?) -> Unit,
                  failure: (ApiError) -> Unit ,
                  terminate: () -> Unit ): Disposable


    fun getCurrentUser( success: (List<UserInfo>?)  ->Unit, failure: (ApiError)->Unit, terminate: () -> Unit):Disposable

    fun signUpUser(userName: String, email: String, password: String,
                   success: (UserInfo?) ->Unit,
                   failure: (ApiError)->Unit,
                   terminate: () -> Unit): Disposable
}