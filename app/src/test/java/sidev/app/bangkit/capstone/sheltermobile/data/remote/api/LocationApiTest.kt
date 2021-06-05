package sidev.app.bangkit.capstone.sheltermobile.data.remote.api

import com.google.gson.Gson
import org.junit.After
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import sidev.app.bangkit.capstone.sheltermobile.DummyTest
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.AppRetrofit
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.LocationApi
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.Requests
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.GeneralCityListResponse
import sidev.lib.console.prin

//TODO 5 Juni 2021: Pisahkan Api tuk lokasi dg bencana.
class LocationApiTest {
    companion object {
        val api: LocationApi by lazy { AppRetrofit.locationApi }
    }

    private lateinit var response: Response<GeneralCityListResponse>
    private lateinit var expectedList: List<List<String>>

    @Test
    fun getEarthQuakeLocations() {
        val gsona = Gson().toJson(Requests.getGempaCity)
        prin("gsona= $gsona")

        val call = api.getLocationBasedOnDisaster(Requests.getGempaCity)
        //(Requests.getGempaCity.also { prin("reqBody = $it") })
        response = call.execute()

        expectedList = DummyTest.earthQuakeCityResponsesRaw
    }

    @After
    fun check(){
        assert(response.isSuccessful)
        prin("res raw body = '${response.raw().body?.toString()}'")
        prin("res code = '${response.code()}'")
        prin("res msg = '${response.message()}'")
        //prin("res msg = '${res.raw().}'")

        val data = response.body()

        Assert.assertNotNull(data)

        val cityList = data!!.nestedcityList
        Assert.assertNotNull(cityList)

        val cityListRaw = cityList.map { it.cityList }

        assert(cityList.isNotEmpty())
        Assert.assertEquals(expectedList, cityListRaw)
    }
}