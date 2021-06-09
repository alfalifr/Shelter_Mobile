package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api

import com.google.gson.Gson
import com.google.gson.JsonParser
import okhttp3.ResponseBody
import retrofit2.Converter
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.ImgurUploadResponse
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
///*
object ImgUploadResponseConverter: Converter<ResponseBody, ImgurUploadResponse> {
    private val gson: Gson by lazy { Gson() }
    private val parser: JsonParser by lazy { JsonParser() }

    override fun convert(value: ResponseBody): ImgurUploadResponse? {
        val str = value.string()
        val oldRes = gson.fromJson(str, ImgurUploadResponse::class.java)

        val jsonObj = parser.parse(str).asJsonObject
        if(jsonObj[Const.KEY_STATUS].asInt != Const.CODE_OK)
            return oldRes

        val linkStr = jsonObj[Const.KEY_DATA].asJsonObject[Const.KEY_LINK].asString
            .replace('\\', '/')

        return oldRes.copy(data = oldRes.data.copy(link = linkStr))
    }
}
// */