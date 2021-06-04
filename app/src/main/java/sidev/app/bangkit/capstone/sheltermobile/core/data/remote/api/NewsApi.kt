package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.NewsBody
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.NewsResponse
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const

interface NewsApi {
    @POST(Const.API_SHELTER)
    fun getNews(@Body data: NewsBody): Call<List<NewsResponse>>
}