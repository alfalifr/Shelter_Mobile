package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request

import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const.Disaster.EARTH_QUAKE
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const.Disaster.FLOOD
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const.Disaster.LANDSLIDE
import java.util.*

object Requests {
    //const val EARTH_QUAKE = "gempa"
    //const val LANDSLIDE = "longsor"
    //const val FLOOD = "banjir"
    val FOREST_FIRE = Const.Disaster.FOREST_FIRE_SHORT.toLowerCase(Locale.ROOT) //"karhutla"

    const val CITY = "city"
    const val WEATHER = "cuaca"

    val getGempaCity: GeneralRequestBodyImpl by lazy { getDisasterCityReqBody(EARTH_QUAKE.toLowerCase(Locale.ROOT)) }
    val getLongsorCity: GeneralRequestBodyImpl by lazy { getDisasterCityReqBody(LANDSLIDE.toLowerCase(Locale.ROOT)) }
    val getKarhutlaCity: GeneralRequestBodyImpl by lazy { getDisasterCityReqBody(FOREST_FIRE) }
    //val getBanjirCity: GeneralRequestBodyImpl by lazy { getDisasterCityReqBody(FLOOD.toLowerCase(Locale.ROOT)) }
    val getBanjirVillage: GeneralRequestBodyImpl by lazy { GeneralRequestBodyImpl("desa_${FLOOD.toLowerCase(Locale.ROOT)}") }

    fun getGeneralReqBody(requestType: String): GeneralRequestBodyImpl = GeneralRequestBodyImpl(requestType)

    fun getDisasterCityAttrib(disaster: String): String = "${CITY}_$disaster"
    fun getDisasterCityReqBody(disaster: String): GeneralRequestBodyImpl = getGeneralReqBody(getDisasterCityAttrib(disaster))

    fun getFilterBody(requestType: String, filter: String): FilterBody = FilterBody(requestType, filter)
    fun getGempaBody(cityName: String): FilterBody = getFilterBody(EARTH_QUAKE.toLowerCase(Locale.ROOT), cityName)
    fun getLongsorBody(cityName: String): FilterBody = getFilterBody(LANDSLIDE.toLowerCase(Locale.ROOT), cityName)
    fun getKarhutlaBody(cityName: String): FilterBody = getFilterBody(FOREST_FIRE.toLowerCase(Locale.ROOT), cityName)
    fun getBanjirBody(cityName: String): FilterBody = getFilterBody(FLOOD.toLowerCase(Locale.ROOT), cityName)

    fun getCuacaBody(minDate: String, maxDate: String): GeneralMinMaxBodyImpl = GeneralMinMaxBodyImpl(
        WEATHER, minDate, maxDate
    )
}