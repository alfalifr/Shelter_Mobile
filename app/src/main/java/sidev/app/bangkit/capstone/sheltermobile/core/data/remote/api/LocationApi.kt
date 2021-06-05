package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.GeneralRequestBodyImpl
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.GeneralCityListResponse
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const

interface LocationApi {
    @POST(Const.API_SHELTER)
    fun getLocationBasedOnDisaster(@Body body: GeneralRequestBodyImpl): Call<GeneralCityListResponse>
}