package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.ResponseBody
import retrofit2.Converter
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.GeneralCityListResponse
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.GeneralCityResponse

object CityListConverter: Converter<ResponseBody, GeneralCityListResponse> {
    private val parser = JsonParser()
    override fun convert(value: ResponseBody): GeneralCityListResponse {
        val str = value.string()
        val array = parser.parse(str).asJsonArray

        val list = array.map {
            val arr = it.asJsonArray
            val nestedList = arr.map {
                it.asString
            }
            GeneralCityResponse(nestedList)
        }
        return GeneralCityListResponse(list)
    }
}