package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response

import com.google.gson.annotations.SerializedName

data class FloodResponse(
    @SerializedName("alamat_lengkap")
    val address: String,

    @SerializedName("desa")
    val village: String,

    @SerializedName("lat")
    val latitude: Double,

    @SerializedName("lon")
    val longitude: Double,

    @SerializedName("kondisi")
    val condition: String,
)