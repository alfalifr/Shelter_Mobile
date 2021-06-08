package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.http.FormUrlEncoded
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.GeneralCityListResponse
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.GeneralCityResponse

object CityListConverter: Converter<ResponseBody, GeneralCityListResponse> {
    private val parser = JsonParser()
    override fun convert(value: ResponseBody): GeneralCityListResponse {
        val str = value.string()
        val array = parser.parse(str).asJsonArray

        val list = array.map {
            //val arr = it.asJsonArray
            val obj = it.asJsonObject

            val nestedList = obj.keySet().map {
                obj[it].asString
            }
/*
            val nestedList = arr.map {
                it.asString
            }
 */
            GeneralCityResponse(nestedList)
        }
        return GeneralCityListResponse(list)
    }
}

/*
object CityListConverter2: Converter<GeneralCityListResponse, RequestBody> {
    override fun convert(value: GeneralCityListResponse): RequestBody? {
        return RequestBody.Companion.
    }
}
 */

/*
interface ag{
    fun afa(@FormUrlEncoded)
}

 */
