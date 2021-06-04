package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response

import com.google.gson.annotations.SerializedName

data class LandslideResponse(
    @SerializedName("kondisi")
    val condition: String,

    @SerializedName("lokasi")
    val cityName: String
)