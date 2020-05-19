package com.demo.myideas.data.network

import com.demo.myideas.data.model.Idea
import com.demo.myideas.data.model.LoginResponse
import com.demo.myideas.data.model.UserInfo
import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.http.*


interface WebService {
    @POST("/users")
    @FormUrlEncoded
    fun signUp(
        @Field("email") email: String?,
        @Field("name") name: String?,
        @Field("password") password: String?
    ): Observable<LoginResponse?>?


    @POST("/access-tokens")
    @FormUrlEncoded
    fun loginUser(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Observable<LoginResponse?>?


    @POST("/access-tokens/refresh")
    @FormUrlEncoded
    fun refreshToken(
        @Field("refresh_token") refreshToken: String?
    ): Observable<LoginResponse?>?

    @GET("/me")
    fun getMe(@Header("X-Access-Token") token: String): Observable<UserInfo?>?


    @GET("/ideas")
    fun getIdeas(@Header("X-Access-Token") token: String,@Query("page") id: Int): Observable<List<Idea>?>?


    @POST("/ideas")
    fun createIdea(@Header("X-Access-Token") token: String,@Body idea: Idea): Observable<Idea>?


    @DELETE("/ideas/{id}")
    fun deleteIdea(@Header("X-Access-Token") token: String,@Path("id") id: String): Completable

    @PUT("/ideas/{id}")
    fun updateIdea(@Header("X-Access-Token") token: String,@Body idea: Idea,@Path("id") id: String): Observable<Idea>?

    @HTTP(method = "DELETE", path = "/access-tokens", hasBody = true)
    @FormUrlEncoded
    fun logout(@Header("X-Access-Token") token: String, @Field("refresh_token") refreshToken: String?) :Completable


}