package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.ReportReqBody
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.GeneralResponse
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const

interface ReportApi {
    @POST(Const.API_SHELTER)
    fun sendReport(@Body body: ReportReqBody): Call<GeneralResponse> //TODO
}