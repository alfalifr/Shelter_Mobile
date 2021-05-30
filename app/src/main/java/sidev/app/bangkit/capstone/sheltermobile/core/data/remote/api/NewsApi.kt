package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.NewsBody
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.NewsResponse

interface NewsApi {
    @POST
    fun getNews(@Body data: NewsBody): Call<List<NewsResponse>>
}