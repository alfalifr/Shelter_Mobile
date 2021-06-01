package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const

object AppRetrofit {
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Const.API_ROOT)
        .build()

    val authApi: AuthApi by lazy { retrofit.create(AuthApi::class.java) }
    val newsApi: NewsApi by lazy { retrofit.create(NewsApi::class.java) }
}