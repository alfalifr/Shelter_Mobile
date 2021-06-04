package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response

import com.google.gson.annotations.SerializedName

data class EarthQuakeResponse(
    @SerializedName("Tanggal")
    val date: String,

    val avg_magnitude: Double,

    @SerializedName("Name")
    val cityName: String,
)