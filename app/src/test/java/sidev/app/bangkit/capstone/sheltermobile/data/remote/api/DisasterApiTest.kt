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
}