package com.demo.myideas.data.repository.users

import com.demo.myideas.data.db.AppDatabase
import com.demo.myideas.data.model.LoginResponse
import com.demo.myideas.data.model.UserInfo
import com.demo.myideas.data.network.ApiError
import com.demo.myideas.data.network.WebService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.HttpException
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    private val webService: WebService,
    private val appDatabase: AppDatabase

) : UserRepository {

    init {
        getCurrentUserInfo()
    }


    //login user
    override fun loginUser(
        userEmail: String, userPassword: String, success: (UserInfo?) -> Unit,
        failure: (ApiError) -> Unit,
        terminate: () -> Unit
    ): Disposable {
        return webService.loginUser(
            userEmail,
            userPassword
        )?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())!!.subscribeWith(
            object : DisposableObserver<LoginResponse?>() {
                override fun onComplete() {

                }
                override fun onNext(loginResponse: LoginResponse) {
                    if (loginResponse?.refreshToken != null && loginResponse?.accessToken != null)
                        getMyInfo(
                            loginResponse.accessToken!!,
                            loginResponse?.refreshToken!!,
                            success,
                            failure,
                            terminate
                        )
                }

                override fun onError(e: Throwable) {
                    if (e is HttpException) {
                        val responseBody: ResponseBody? =
                            (e as HttpException).response()?.errorBody()
                        failure(ApiError(e))
                    }
                }
            }
        )
    }


    //get the current user information
    override fun getMyInfo(
        token: String, refreshToken: String, success: (UserInfo?) -> Unit,
        failure: (ApiError) -> Unit,
        terminate: () -> Unit
    ): Disposable {
        return webService.getMe(token)?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())!!.subscribeWith(
            object :
                DisposableObserver<UserInfo>() {
                override fun onComplete() {

                }

                override fun onNext(it: UserInfo) {
                    if (it != null) {
                        it.accessToken = token
                        it.refreshToken = refreshToken
                        val subscribe = appDatabase.userDao().insertNewUser(it)
                            .observeOn(AndroidSchedulers.mainThread()).subscribe {

                                success(it)
                            }
                    }
                }


                override fun onError(e: Throwable) {
                    failure(ApiError(e))
                }
            })
    }





    //getting current user information
    override fun getCurrentUser(success: (List<UserInfo>?) -> Unit,
                                    failure: (ApiError) -> Unit,
                                    terminate: () -> Unit): Disposable{
        return appDatabase.userDao().getUserInfo()?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())!!.subscribeWith(
            object : DisposableObserver<List<UserInfo>>() {
                override fun onComplete() {

                }
                override fun onNext(userInfo: List<UserInfo>) {
                    success(userInfo)
                }
                override fun onError(e: Throwable) {
                    failure(ApiError(e))
                }
            })
    }


    //signing up user
    override fun signUpUser(
        userName: String, email: String, password: String,
        success: (UserInfo?) -> Unit,
        failure: (ApiError) -> Unit,
        terminate: () -> Unit
    ): Disposable {
        return webService.signUp(email,
            userName,
            password
        )?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())!!.subscribeWith(
            object : DisposableObserver<LoginResponse>() {
                override fun onComplete() {

                }
                override fun onNext(loginResponse: LoginResponse) {
                    if (loginResponse?.refreshToken != null && loginResponse?.accessToken != null)
                        getMyInfo(
                            loginResponse.accessToken!!,
                            loginResponse?.refreshToken!!,
                            success,
                            failure,
                            terminate
                        )
                }
                override fun onError(e: Throwable) {
                    failure(ApiError(e))
                }

            }

        )
    }


    private fun getCurrentUserInfo() {
        val subscribe = appDatabase.userDao().getUserInfo().observeOn(
            AndroidSchedulers.mainThread()
        )!!.subscribe {
        }
        subscribe.dispose()
    }




}