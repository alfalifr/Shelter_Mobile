package sidev.app.bangkit.capstone.sheltermobile.core.data.dummy

import sidev.app.bangkit.capstone.sheltermobile.R
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.LoginBody
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.SignupBody
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.*
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import java.lang.IllegalArgumentException
import java.util.*

object Dummy {

/*
=========================
Not supposed to be dummy
=========================
 */
    val disasterList = listOf<Disaster>(
        Disaster(1, "Kebakaran Hutan", "R.drawable.ic_disaster_forest_fire"),
        Disaster(2, "Banjir", "R.drawable.ic_disaster_flood"),
        Disaster(3, "Longsor", "R.drawable.ic_disaster_landslide"),
        //Disaster(4, "Gunung Meletus", "R.drawable.ic_disaster_landslide"),
        Disaster(4, "Gempa", "R.drawable.ic_disaster_earthquake"),
    )

    val emergencyList = listOf<Emergency>(
        Emergency(1, "Hijau", "#32a83e", Const.Emergency.SEVERITY_GREEN),
        Emergency(2, "Kuning", "#e6de09", Const.Emergency.SEVERITY_YELLOW),
        Emergency(3, "Merah", "#e61809", Const.Emergency.SEVERITY_RED),
    )

    val weatherList = listOf<Weather>(
        Weather(1, "berangin", "R.drawable.ic_weather_berangin"),
        Weather(2, "berawan", "R.drawable.ic_weather_berawan"),
        Weather(3, "cerah", "R.drawable.ic_weather_cerah"),
        Weather(4, "cerah berawan", "R.drawable.ic_weather_cerah_berawan"),
        Weather(5, "dingin berangin", "R.drawable.ic_weather_dingin_berangin"),
        Weather(6, "gerimis", "R.drawable.ic_weather_gerimis"),
        Weather(7, "hujan", "R.drawable.ic_weather_hujan"),
        Weather(8, "hujan deras", "R.drawable.ic_weather_hujan_deras"),
        Weather(9, "mendung", "R.drawable.ic_weather_mendung"),
    )


    fun getWeatherByName(name: String): Weather = weatherList.find { it.name.equals(name, true) }
        ?: throw IllegalArgumentException("No such weather data for name '$name'")

    fun getDisasterByName(name: String): Disaster = disasterList.find { it.name.equals(name, ignoreCase = true) }
        ?: throw IllegalArgumentException("No such disaster data for name '$name'")


    fun getLandslideEmergencyByName(name: String): Emergency = when(name.toLowerCase(Locale.ROOT)) {
        Const.Emergency.LANDSLIDE_NORMAL.toLowerCase(Locale.ROOT) -> emergencyList[0]
        Const.Emergency.LANDSLIDE_BIT_DANGER.toLowerCase(Locale.ROOT) -> emergencyList[1]
        Const.Emergency.LANDSLIDE_DANGER.toLowerCase(Locale.ROOT) -> emergencyList[2]
        Const.Emergency.LANDSLIDE_VERY_DANGER.toLowerCase(Locale.ROOT) -> emergencyList[2]
        else -> throw IllegalArgumentException("No such landslide emergency data for name '$name'")
    }

    fun getEarthQuakeEmergencyByScale(scale: Double): Emergency = when(scale) {
        in Const.Emergency.EARTH_QUAKE_GREEN -> emergencyList[0]
        in Const.Emergency.EARTH_QUAKE_YELLOW -> emergencyList[1]
        in Const.Emergency.EARTH_QUAKE_RED -> emergencyList[2]
        else -> throw IllegalArgumentException("No such earth quake emergency data for scale '$scale'")
    }

    fun getFireForestEmergencyByScale(scale: Int): Emergency = when(scale) {
        in Const.Emergency.FIRE_FOREST_GREEN -> emergencyList[0]
        in Const.Emergency.FIRE_FOREST_YELLOW -> emergencyList[1]
        in Const.Emergency.FIRE_FOREST_RED -> emergencyList[2]
        else -> throw IllegalArgumentException("No such fire forest emergency data for scale '$scale'")
    }
/*
=========================
Dummy section
=========================
 */

    val timestampList = listOf<TimeString>(
        //Util.getTimeString("2021-05-29 21:31:51"),
        //Util.getTimeString("2021-05-30 21:31:51"),
        //Util.getTimeString("2021-05-31 21:31:51"),
        Util.getTimeString(millisOffset = Const.TIME_OFFSET),
        Util.getTimeString(millisOffset = Const.TIME_OFFSET * 2),
        Util.getTimeString(millisOffset = Const.TIME_OFFSET * 3),
    )
    val userList = listOf<User>(
        User("b@b.b", "bayu", 'M'),
        User("a@a.a", "ayu", 'M'),
    )

    val userPswd = listOf<String>(
        "abc123",
        "a1",
    )

    fun getLoginBody(i: Int): LoginBody = LoginBody(userList[i].email, userPswd[i])
    fun getSignupBody(i: Int): SignupBody = userList[i].let { SignupBody(it.email, userPswd[i], it.name, it.gender) }


    val locationList = listOf<Location>(
        Location(1, "Medan", Coordinate(0.0,0.0)),
        Location(2, "Palembang", Coordinate(0.0,0.0)),
        Location(3, "Bandung", Coordinate(0.0,0.0)),
        //Location(4, "Surabaya", Coordinate(0.0,0.0)),
        //Location(5, "Jakart", Coordinate(0.0,0.0)),
    )

    val newsList = listOf<News>(
        News(timestampList[0], "Ini CLickbait", "Ini juga sama aja", "https://i.pinimg.com/originals/e1/1b/f8/e11bf8a6206dda58d61dfc004b813516.jpg", "https://i.pinimg.com/originals/e1/1b/f8/e11bf8a6206dda58d61dfc004b813516.jpg", Const.TYPE_NEWS),
        News(timestampList[1], "Ini CLickbait 2", "Ini ya gitu", "https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/1a052c60-b6a8-476f-87d9-04b7f5f0f903/d52ha02-7eebab18-a875-4185-94de-6fd44be0206a.jpg/v1/fill/w_900,h_675,q_75,strp/smiling_cat_by_omniamohamed_d52ha02-fullview.jpg?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7ImhlaWdodCI6Ijw9Njc1IiwicGF0aCI6IlwvZlwvMWEwNTJjNjAtYjZhOC00NzZmLTg3ZDktMDRiN2Y1ZjBmOTAzXC9kNTJoYTAyLTdlZWJhYjE4LWE4NzUtNDE4NS05NGRlLTZmZDQ0YmUwMjA2YS5qcGciLCJ3aWR0aCI6Ijw9OTAwIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmltYWdlLm9wZXJhdGlvbnMiXX0.uEgwgBfq7faRJ9GPWtsJLmc8_kqG11HHAfdtNX5j56A", "https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/1a052c60-b6a8-476f-87d9-04b7f5f0f903/d52ha02-7eebab18-a875-4185-94de-6fd44be0206a.jpg/v1/fill/w_900,h_675,q_75,strp/smiling_cat_by_omniamohamed_d52ha02-fullview.jpg?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7ImhlaWdodCI6Ijw9Njc1IiwicGF0aCI6IlwvZlwvMWEwNTJjNjAtYjZhOC00NzZmLTg3ZDktMDRiN2Y1ZjBmOTAzXC9kNTJoYTAyLTdlZWJhYjE4LWE4NzUtNDE4NS05NGRlLTZmZDQ0YmUwMjA2YS5qcGciLCJ3aWR0aCI6Ijw9OTAwIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmltYWdlLm9wZXJhdGlvbnMiXX0.uEgwgBfq7faRJ9GPWtsJLmc8_kqG11HHAfdtNX5j56A", Const.TYPE_NEWS)
    )
    val articleList = listOf<News>(
        News(timestampList[0], "Ini CLickbait 3", "Ini juga sama aja", "https://i.pinimg.com/originals/e1/1b/f8/e11bf8a6206dda58d61dfc004b813516.jpg", "https://i.pinimg.com/originals/e1/1b/f8/e11bf8a6206dda58d61dfc004b813516.jpg", Const.TYPE_ARTICLE),
        News(timestampList[1], "Ini CLickbait 4", "Ini ya gitu", "https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/1a052c60-b6a8-476f-87d9-04b7f5f0f903/d52ha02-7eebab18-a875-4185-94de-6fd44be0206a.jpg/v1/fill/w_900,h_675,q_75,strp/smiling_cat_by_omniamohamed_d52ha02-fullview.jpg?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7ImhlaWdodCI6Ijw9Njc1IiwicGF0aCI6IlwvZlwvMWEwNTJjNjAtYjZhOC00NzZmLTg3ZDktMDRiN2Y1ZjBmOTAzXC9kNTJoYTAyLTdlZWJhYjE4LWE4NzUtNDE4NS05NGRlLTZmZDQ0YmUwMjA2YS5qcGciLCJ3aWR0aCI6Ijw9OTAwIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmltYWdlLm9wZXJhdGlvbnMiXX0.uEgwgBfq7faRJ9GPWtsJLmc8_kqG11HHAfdtNX5j56A", "https://emojipedia.org/grinning-cat/", Const.TYPE_ARTICLE)
    )
    val emptyNews = News(TimeString("", ""), "", "", "", "", Const.TYPE_NEWS)

    val warningList1 = locationList[0].let { location ->
        listOf<WarningStatus>(
            WarningStatus(disasterList[0], emergencyList[1], "Mayan", timestampList[0], location, ""),
            WarningStatus(disasterList[0], emergencyList[2], "Hati2 Gosong", timestampList[1], location, ""),
            WarningStatus(disasterList[1], emergencyList[0], "Aman Bro", timestampList[0], location, ""),
            WarningStatus(disasterList[3], emergencyList[1], "Jangan lengah", timestampList[0], location, ""),
        )
    }
    val warningList2 = locationList[1].let { location ->
        listOf<WarningStatus>(
            WarningStatus(disasterList[0], emergencyList[0], "Tidur Nyenyak", timestampList[0], location, ""),
            WarningStatus(disasterList[0], emergencyList[1], "Ok", timestampList[1], location, ""),
            WarningStatus(disasterList[1], emergencyList[0], "Aman Bro", timestampList[0], location, ""),
            WarningStatus(disasterList[3], emergencyList[2], "Tidur tak sleeping", timestampList[0], location, ""),
        )
    }
    val warningList3 = locationList[2].let { location ->
        listOf<WarningStatus>(
            WarningStatus(disasterList[0], emergencyList[0], "Sante", timestampList[0], location, ""),
            WarningStatus(disasterList[0], emergencyList[1], "Ok", timestampList[1], location, ""),
            WarningStatus(disasterList[1], emergencyList[2], "Banjir kan datang", timestampList[0], location, ""),
            WarningStatus(disasterList[2], emergencyList[0], "Santuy", timestampList[0], location, ""),
            WarningStatus(disasterList[3], emergencyList[2], "Bumi mau marah", timestampList[0], location, ""),
        )
    }
    val warningListAll = warningList1 + warningList2 + warningList3

    val weatherForecastList = listOf<WeatherForecast>(
        WeatherForecast(weatherList[0], 10f, 11f, 25f, 180f, 10f, timestampList[1]),
        WeatherForecast(weatherList[3], 14f, 12f, 256f, 110f, 3012f, timestampList[2]),
    )


    val formList = listOf<Form>(
        Form(timestampList[0], "Ini judul 1", "Ini desc 1", emptyList()),
        Form(timestampList[1], "Ini judul 2", "Ini desc 2", emptyList()),
    )


    fun getWarningStatusList(disasterId: Int, locationId: Int, startTimestamp: String): List<WarningStatus> = warningListAll.filter {
        it.disaster.id == disasterId && it.location.id == locationId
    }
    fun getWarningDetailList(disasterId: Int, locationId: Int, startTimestamp: String): List<WarningDetail> = warningListAll.filter {
        it.disaster.id == disasterId && it.location.id == locationId
    }.map { WarningDetail(it, "Ini deskripsi", newsList[0]) }

    fun <T> getEmptyListResult(): Success<List<T>> = Success(emptyList(), 0)
}