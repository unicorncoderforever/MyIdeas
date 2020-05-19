package com.demo.myideas.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("jwt")
    var accessToken: String? ,
    @SerializedName("refresh_token")
    var refreshToken: String?
    )
