package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.*
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.*
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const

interface DisasterApi {
    @POST(Const.API_SHELTER)
    fun getLandslidePredictions(@Body body: LandslideBody): Call<List<LandslideResponse>>

    @POST(Const.API_SHELTER)
    fun getEarthQuakePredictions(@Body body: EarthQuakeBody): Call<List<EarthQuakeResponse>>

    @POST(Const.API_SHELTER)
    fun getFloodPredictions(@Body body: FloodBody): Call<List<FloodResponse>>
/*
    @POST(Const.API_SHELTER)
    fun getDisasterLocations(@Body body: GeneralRequestBodyImpl): Call<GeneralCityListResponse>
 */
}