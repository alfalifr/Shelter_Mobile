package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.EarthQuakeBody
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.GeneralRequestBody
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.GeneralRequestBodyImpl
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.LandslideBody
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.EarthQuakeResponse
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.GeneralCityListResponse
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.GeneralCityResponse
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.LandslideResponse
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const

interface DisasterApi {
    @POST(Const.API_SHELTER)
    fun getLandslidePredictions(@Body body: LandslideBody): Call<List<LandslideResponse>>

    @POST(Const.API_SHELTER)
    fun getEarthQuakePredictions(@Body body: EarthQuakeBody): Call<List<EarthQuakeResponse>>
/*
    @POST(Const.API_SHELTER)
    fun getDisasterLocations(@Body body: GeneralRequestBodyImpl): Call<GeneralCityListResponse>
 */
}