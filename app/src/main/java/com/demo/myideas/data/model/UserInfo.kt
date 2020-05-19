package com.demo.myideas.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "user_info")
data class UserInfo (
    @SerializedName("jwt")
    var accessToken: String? ,
    @SerializedName("refresh_token")
    var refreshToken: String?,
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @SerializedName("name")
    var name: String?,
    @SerializedName("email")
    var email: String,
    @SerializedName("avatar_url")
    var avtar_url: String?
): Parcelable {
    override fun toString(): String {
        return "userinfo(content='$name', avg='$email')"
    }
}