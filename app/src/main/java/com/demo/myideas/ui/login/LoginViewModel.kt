package com.demo.myideas.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.demo.myideas.data.model.UserInfo
import com.demo.myideas.data.network.ApiError
import com.demo.myideas.data.repository.users.UserRepository
import com.demo.myideas.ui.base.BaseViewModel
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val userRepository: UserRepository) :
    BaseViewModel() {

    val mUserInfo = MutableLiveData<UserInfo>()
    val error: MutableLiveData<ApiError> by lazy { MutableLiveData<ApiError>() }
    val TAG = "LoginViewModel"

    fun getCurrentUserInfo() {
        userRepository.getCurrentUser({ userInfo ->
            if (userInfo != null && userInfo.size > 0) {
                mUserInfo.value = userInfo[0]
            } else {
                mUserInfo.value = null
            }
        }, {
            error.value = it
        }, {
            Log.d(TAG, "getNewsData.terminate() called")
        })
    }

    fun loginUser(
        userEmail: String, password: String, success: () -> Unit,
        failure: (ApiError) -> Unit
    ) {
        userRepository.loginUser(userEmail, password, { userInfo ->
            mUserInfo.value = userInfo
            success()
        }, {
            error.value = it
            failure(it)
        }, {
            Log.d(TAG, "getNewsData.terminate() called")
        })
    }

    fun signUpUser(
        name: String, userEmail: String, password: String,
        success: () -> Unit,
        failure: (ApiError) -> Unit
    ) {
        userRepository.signUpUser(name, userEmail, password, { userInfo ->
            mUserInfo.value = userInfo
            success()
        }, {
            error.value = it
            failure(it)
        }, {
            Log.d(TAG, "getNewsData.terminate() called")
        })
    }


}