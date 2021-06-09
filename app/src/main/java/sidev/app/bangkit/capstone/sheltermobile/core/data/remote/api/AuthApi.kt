package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.LoginBody
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.SignupBody
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.UpdateProfileReqBody
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.GeneralResponse
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.GeneralShortResponse
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.LoginResponse
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const

interface AuthApi {
    @POST(Const.API_SHELTER)
    fun login(@Body data: LoginBody): Call<LoginResponse>

    @POST(Const.API_SHELTER)
    fun signup(@Body data: SignupBody): Call<GeneralResponse>

    @Headers("Content-Type: application/json")
    @POST(Const.API_SHELTER)
    fun updateProfile(@Body data: UpdateProfileReqBody): Call<GeneralShortResponse>
}