package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.LoginBody
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.SignupBody
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.GeneralResponse
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.LoginResponse

interface AuthApi {
    @POST("shelter_api.php")
    fun login(@Body data: LoginBody): Call<LoginResponse>

    @POST("shelter_api.php")
    fun signup(@Body data: SignupBody): Call<GeneralResponse>
}