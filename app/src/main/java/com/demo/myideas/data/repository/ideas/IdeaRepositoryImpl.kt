package com.demo.myideas.data.repository.ideas

import android.util.Log
import com.demo.myideas.data.db.AppDatabase
import com.demo.myideas.data.model.Idea
import com.demo.myideas.data.model.LoginResponse
import com.demo.myideas.data.model.UserInfo
import com.demo.myideas.data.network.ApiError
import com.demo.myideas.data.network.WebService
import com.demo.myideas.utility.Utility
import com.demo.myideas.utility.Utility.Companion.getResponseCode
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

class IdeaRepositoryImpl @Inject constructor(
    private val webService: WebService,
    private val appDatabase: AppDatabase
) : IdeasRepository {
    private var mUserInfo: UserInfo? = null
    private var ideas: List<Idea>? = ArrayList<Idea>()
    val TAG = "IdeaRepositoryImpl";

    init {
        getCurrentUserInfo()
    }


    //fetching ideas
    override fun fetchIdeas(
        itemPerPage: Int,
        success: (MutableList<Idea>?) -> Unit,
        failure: (ApiError) -> Unit): Disposable {
        return if (mUserInfo?.accessToken != null) {
            fetchFromServer(itemPerPage, success, failure)
        } else {
            fetchUserInfo(success = { fetchIdeas(itemPerPage,success,failure) }, failure = failure)
        }
    }

    //inserting new ideas
    override fun insertIdea(
        idea: Idea,
        success: (Idea) -> Unit,
        failure: (ApiError) -> Unit): Disposable {
        return webService.createIdea(
            mUserInfo?.accessToken!!,
            idea
        )?.subscribeOn(Schedulers.io())?.observeOn(
            AndroidSchedulers.mainThread()
        )!!.subscribeWith(object : DisposableObserver<Idea>() {
            override fun onComplete() {}
            override fun onNext(idea: Idea) {
                val subscribe = appDatabase.IdeaDao().insertIdea(idea).observeOn(
                    AndroidSchedulers.mainThread()
                )!!.subscribe {
                    success(idea)
                }
            }
            override fun onError(e: Throwable) {
                if (getResponseCode(e) != 401) {
                    refreshToken(
                        success = { insertIdea(idea, success, failure) },
                        failure = failure
                    )
                } else {
                    failure(ApiError(e))
                }
            }

        })

    }


    //getting current user information
    private fun getCurrentUserInfo() {
        val subscribe = appDatabase.userDao().getUserInfo().observeOn(
            AndroidSchedulers.mainThread()
        )!!.subscribe {
            if(it.size > 0)
            mUserInfo = it[0]
        }
    }


    //fetching local data
    private fun fetchLocalData(
        success: (MutableList<Idea>?) -> Unit) {
        val subscribe = appDatabase.IdeaDao().getAllIdeas().observeOn(
            AndroidSchedulers.mainThread()
        )!!.subscribe {
            success(it.toMutableList())
        }
    }


    //fetching data server
    private fun fetchFromServer(
        itemPerPage: Int,
        success: (MutableList<Idea>?) -> Unit,
        failure: (ApiError) -> Unit
    ): Disposable {
        return webService.getIdeas(mUserInfo?.accessToken!!, itemPerPage)?.subscribeOn(
            Schedulers.io()
        )?.observeOn(
            AndroidSchedulers.mainThread()
        )!!.subscribeWith(object : DisposableObserver<List<Idea>>() {
            override fun onComplete() {

            }
            override fun onNext(ideas: List<Idea>) {
                val subscribe = this@IdeaRepositoryImpl.appDatabase.IdeaDao().insertAllIdea(ideas)
                    .observeOn(AndroidSchedulers.mainThread()).subscribe {
//                    success(ideas.toMutableList())
                    fetchLocalData(success)
                }
            }
            override fun onError(e: Throwable) {
                if (e is HttpException) {
                    val code = e.response()!!.code()
                    if (code == 401) {
                        Log.e(TAG,"NOT_AUTHORIZED")
                        refreshToken(success = {
                            Log.i(TAG,"refresh token try success")
                            fetchFromServer(itemPerPage, success, failure) }, failure = failure)
                    } else {
                        fetchLocalData( success)
                    }
                } else {
                    fetchLocalData( success)
                }
            }
        })
    }



    //refreshing token
    private fun refreshToken(
        success: () -> Unit,
        failure: (ApiError) -> Unit): Disposable {
        return webService.refreshToken(mUserInfo?.refreshToken)?.subscribeOn(Schedulers.io())?.observeOn(
            AndroidSchedulers.mainThread()
        )!!.subscribeWith(object : DisposableObserver<LoginResponse>() {
            override fun onComplete() {

            }
            override fun onNext(response: LoginResponse) {
                //refresh token successful update the database and continue with the previous operation
                mUserInfo?.accessToken = response.accessToken
                val subscribe = appDatabase.userDao().updateIdea(mUserInfo!!).observeOn(
                    AndroidSchedulers.mainThread()
                )!!.subscribe {
                    Log.e(TAG, "success idea update database")
                    success()
                }
            }
            override fun onError(e: Throwable) {
                //if the refresh token fails user must be notifed (may be user has been removed
                failure(ApiError(e))
            }
        })
    }




    //fetching user information
    private fun fetchUserInfo(success: () -> Unit,
        failure: (ApiError) -> Unit
       ): Disposable {
        return appDatabase?.userDao().getUserInfo().subscribeOn(Schedulers.io())?.observeOn(
            AndroidSchedulers.mainThread()
        )!!.subscribeWith(object : DisposableObserver<List<UserInfo>>() {
            override fun onComplete() {

            }
            override fun onNext(userInfo: List<UserInfo>) {
                if (userInfo.isNotEmpty() && userInfo[0].accessToken != null) {
                    mUserInfo = userInfo[0]
                    success()
                }
            }
            override fun onError(e: Throwable) {
                  failure(ApiError(e))
            }

        })
    }





    //updating ideas
    override fun updateIdeas(idea: Idea, success: (Idea) -> Unit, failure: (ApiError) -> Unit): Disposable {
        return  webService.updateIdea(
            mUserInfo?.accessToken!!,
            idea,idea.id
        )?.subscribeOn(Schedulers.io())?.observeOn(
            AndroidSchedulers.mainThread()
        )!!.subscribeWith(object : DisposableObserver<Idea>() {
            override fun onComplete() {}
            override fun onNext(idea: Idea) {
                Log.e(TAG, "success update idea from the server")
                val subscribe = appDatabase.IdeaDao().updateIdea(idea).observeOn(
                    AndroidSchedulers.mainThread()
                ).subscribe {
                    success(idea)
                }
            }

           override fun onError(e: Throwable) {
                if(getResponseCode(e) == 401){
                    refreshToken(success = { updateIdeas(idea,success,failure) },failure = failure)
                }else{
                    failure(ApiError(e))
                }
            }

        })
    }



    //deleting ideas
    override fun deleteIdea(
        idea: Idea,
        success: (Idea) -> Unit,
        failure: (ApiError) -> Unit,
        terminate: () -> Unit): Disposable {
        return webService.deleteIdea(
            mUserInfo?.accessToken!!,
            idea.id
        ).subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread()).subscribeWith(object : DisposableCompletableObserver() {
            override fun onComplete() {
                val subscribe = this@IdeaRepositoryImpl.appDatabase.IdeaDao().deleteIdea(idea)
                    .observeOn(AndroidSchedulers.mainThread()).subscribe {
                        success(idea)
                    }
            }
            override fun onError(e: Throwable) {
                if(getResponseCode(e) == 401){
                    Log.e(TAG,"Token Refresh while update operation")
                    refreshToken(success = { deleteIdea(idea,success,failure) },failure = failure)
                }else{
                    failure(ApiError(e))
                }
            }

        })


    }

    override fun logoutUser(success: () -> Unit, failure: (ApiError) -> Unit):Disposable {
        if(mUserInfo?.accessToken != null && mUserInfo?.refreshToken != null) {
            return webService.logout(
                mUserInfo?.accessToken!!,
                mUserInfo?.refreshToken
            ).subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver() {
                    override fun onComplete() {
                        val subscribe =
                            this@IdeaRepositoryImpl.appDatabase.userDao().deleteUserToken()
                                .observeOn(AndroidSchedulers.mainThread()).subscribe {
                                    this@IdeaRepositoryImpl.appDatabase.IdeaDao().deleteAllIdea()
                                        .observeOn(AndroidSchedulers.mainThread()).subscribe {
                                            mUserInfo = null
                                            success()
                                        }
                                }
                    }

                    override fun onError(e: Throwable) {
                        if (Utility.getResponseCode(e) == 401) {
                            Log.e(com.demo.myideas.ui.base.TAG, "Token Refresh while update operation")
                            refreshToken(
                                success = { logoutUser(success, failure) },
                                failure = failure
                            )
                        } else {
                            failure(ApiError(e))
                        }
                    }

                })
        }else{

            return refreshToken({
                logoutUser(success,failure)
            },failure)
        }

    }



}