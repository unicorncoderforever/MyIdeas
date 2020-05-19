package com.demo.myideas.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "ideas")
data class Idea (
    @PrimaryKey
    @SerializedName("id")
    val id: String,

    @SerializedName("content")
    var content: String? = "",

    @SerializedName("impact")
    var impact: Int? = 0,

    @SerializedName("ease")
    var ease: Int? = 0,

    @SerializedName("confidence")
    var confidence: Int? = 0,

    @SerializedName("average_score")
    var avg: Float? = 0.0f,

    @SerializedName("created_at")
    var createdAt: Long? = 0

) : Parcelable {
    override fun toString(): String {
        return "idea(content='$content', avg='$avg')"
    }
}