package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const

object AppRetrofit {
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(ConverterFactory)
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
                .build()
        )
        .baseUrl(Const.API_ROOT)
        .build()

    val authApi: AuthApi by lazy { retrofit.create(AuthApi::class.java) }
    val newsApi: NewsApi by lazy { retrofit.create(NewsApi::class.java) }
    val disasterApi: DisasterApi by lazy { retrofit.create(DisasterApi::class.java) }
    val weatherApi: WeatherApi by lazy { retrofit.create(WeatherApi::class.java) }
}