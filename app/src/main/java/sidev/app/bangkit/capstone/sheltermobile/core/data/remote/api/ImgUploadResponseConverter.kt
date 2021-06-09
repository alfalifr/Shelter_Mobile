package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api

import com.google.gson.Gson
import com.google.gson.JsonParser
import okhttp3.ResponseBody
import retrofit2.Converter
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.GeneralCityListResponse
/*
object ImgUploadResponseConverter: Converter<ResponseBody, GeneralCityListResponse> {
    private val gson: Gson by lazy { Gson() }
    private val parser: JsonParser by lazy { JsonParser() }

    override fun convert(value: ResponseBody): GeneralCityListResponse? {
        val str = value.string()
        val oldRes = gson()
    }
}
 */