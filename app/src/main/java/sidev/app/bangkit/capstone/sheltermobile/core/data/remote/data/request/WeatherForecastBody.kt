package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request

import com.google.gson.annotations.SerializedName

data class WeatherForecastBody(
    @SerializedName("min")
    override val minDate: String,
    @SerializedName("max")
    override val maxDate: String
): GeneralMinMaxBody {
    override val _requestType: String = Requests.WEATHER
}