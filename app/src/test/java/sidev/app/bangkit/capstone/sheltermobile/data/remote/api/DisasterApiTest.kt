package sidev.app.bangkit.capstone.sheltermobile.data.remote.api

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import sidev.app.bangkit.capstone.sheltermobile.DummyTest
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.AppRetrofit
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.DisasterApi
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.*
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.EarthQuakeResponse
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.GeneralCityResponse
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.LandslideResponse
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.lib.console.prin
import sidev.lib.console.prine
import java.util.concurrent.CountDownLatch

class DisasterApiTest {
    companion object {
        val api: DisasterApi by lazy { AppRetrofit.disasterApi }
    }

    @Test
    fun getLandslidePredictions() {
        val call = api.getLandslidePredictions(DummyTest.landslideBody)
        val res = call.execute()

        assert(res.isSuccessful)

        val data = res.body()
        prin("data = $data")

        assertNotNull(data)
        assert(data!!.isNotEmpty())
        assertEquals(DummyTest.landslideResponse, data.first())
    }

    @Test
    fun getEarthQuakePredictions() {
        val call = api.getEarthQuakePredictions(DummyTest.earthQuakeBody.also { prin("reqBody = ${Gson().toJson(it)}") })
        val res = call.execute()

        assert(res.isSuccessful)

        val data = res.body()
        prin("data = $data")

        assertNotNull(data)
        assert(data!!.isNotEmpty())
        assertEquals(DummyTest.earthQuakeResponse, data.first())
    }

    @Test
    fun getEarthQuakeLocations() {
        val gsona = Gson().toJson(Requests.getGempaCity)
        prin("gsona= $gsona")

        val call = api.getDisasterLocations(GeneralRequestBodyImpl("city_gempa"))
        //(Requests.getGempaCity.also { prin("reqBody = $it") })
        val res = call.execute()
/*
        val latch = CountDownLatch(1)
        val res = call.enqueue(object: Callback<List<String>> {
            /**
             * Invoked for a received HTTP response.
             *
             *
             * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
             * Call [Response.isSuccessful] to determine if the response indicates success.
             */
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                val body = response.raw().body()?.string()
                prin("body = $body")
                latch.countDown()
            }

            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected exception
             * occurred creating the request or processing the response.
             */
            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                prine("ERROR t = $t")
                t.printStackTrace()
            }
        })
        latch.await()
// */
///*
        assert(res.isSuccessful)
        prin("res raw body = '${res.raw().body()?.toString()}'")
        prin("res code = '${res.code()}'")
        prin("res msg = '${res.message()}'")
        //prin("res msg = '${res.raw().}'")

        val data = res.body()

        assertNotNull(data)

        val cityList = data!!.nestedcityList
        assertNotNull(cityList)

        val cityListRaw = cityList.map { it.cityList }

        assert(cityList.isNotEmpty())
        assertEquals(DummyTest.earthQuakeCityResponsesRaw, cityListRaw)
// */
    }
}