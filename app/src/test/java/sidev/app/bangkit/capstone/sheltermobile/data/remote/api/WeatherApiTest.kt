package sidev.app.bangkit.capstone.sheltermobile.data.remote.api

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import retrofit2.Call
import retrofit2.http.Body
import sidev.app.bangkit.capstone.sheltermobile.DummyTest
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.AppRetrofit
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.WeatherApi
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.WeatherForecastBody
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.WeatherForecastResponse
import sidev.lib.console.prin

class WeatherApiTest {
    companion object {
        val api: WeatherApi by lazy { AppRetrofit.weatherApi }
    }

    @Test
    fun getWeatherForecast() {
        val call = api.getWeatherForecast(DummyTest.weatherForecastBody)
        val res = call.execute()

        assert(res.isSuccessful)

        val list = res.body()
        prin("data = $list")

        assertNotNull(list)
        assert(list!!.isNotEmpty())
        assertEquals(DummyTest.weatherForecastResponse, list.first())
    }
}