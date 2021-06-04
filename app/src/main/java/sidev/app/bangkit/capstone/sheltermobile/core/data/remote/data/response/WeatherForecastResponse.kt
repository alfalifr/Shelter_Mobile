package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response

import com.google.gson.annotations.SerializedName

data class WeatherForecastResponse(
    @SerializedName("Date")
    val date: String,

    @SerializedName("ff_avg avg")
    val windSpeed: Double,

    @SerializedName("RH_avg avg")
    val humidity: Double,

    @SerializedName("RR avg")
    val rainfall: Double,

    @SerializedName("ss avg")
    val ultraviolet: Double,

    @SerializedName("Tavg avg")
    val temperature: Double,

    @SerializedName("cuaca")
    val weatherName: String,
)