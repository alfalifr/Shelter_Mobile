package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api

import retrofit2.Call
import retrofit2.http.*
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.ImageReqBody
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.ImgurUploadResponse
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const

interface ImgApi {
    @Headers(Const.API_KEY_IMGUR)
    @POST(Const.API_IMGUR_IMAGE)
    fun uploadImg(@Body image: ImageReqBody): Call<ImgurUploadResponse>
}