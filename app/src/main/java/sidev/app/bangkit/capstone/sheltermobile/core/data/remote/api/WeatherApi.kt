package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.WeatherForecastBody
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.WeatherForecastResponse
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const

interface WeatherApi {
    @POST(Const.API_SHELTER)
    fun getWeatherForecast(@Body body: WeatherForecastBody): Call<List<WeatherForecastResponse>>
}