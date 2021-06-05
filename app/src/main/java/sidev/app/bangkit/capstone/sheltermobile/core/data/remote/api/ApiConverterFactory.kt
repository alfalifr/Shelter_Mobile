package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.GeneralCityListResponse
import java.lang.reflect.Type


object ApiConverterFactory: Converter.Factory() {
    private val gsonFactory = GsonConverterFactory.create()

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? = when (type) {
        GeneralCityListResponse::class.java -> CityListConverter
        else -> gsonFactory.responseBodyConverter(type, annotations, retrofit)
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? =
        gsonFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit)

    override fun stringConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, String>? =
        gsonFactory.stringConverter(type, annotations, retrofit)
}