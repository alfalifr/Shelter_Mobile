package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request

import com.google.gson.annotations.SerializedName

data class ImageReqBody(
    @SerializedName("image")
    val base64: String,
)