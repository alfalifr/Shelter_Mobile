package sidev.app.bangkit.capstone.sheltermobile.core.util

import retrofit2.Call
import sidev.app.bangkit.capstone.sheltermobile.core.data.dummy.Dummy
import sidev.app.bangkit.capstone.sheltermobile.core.data.entity.*
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.LoginBody
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.ReportReqBody
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.SignupBody
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.UpdateProfileReqBody
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.*
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.*
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.model.DisasterGroup
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*

object DataMapper {
    fun toDisasterGroupList(
        disasterList: List<Disaster>,
        warningStatusList: List<List<WarningStatus>>,
    ): List<DisasterGroup> {
        if(disasterList.size != warningStatusList.size)
            throw IllegalArgumentException("disasterList.size (${disasterList.size}) should be same as warningStatusList.size (${warningStatusList.size}")
        return disasterList.mapIndexed { i, disaster ->
            DisasterGroup(disaster, warningStatusList[i])
        }
    }
    fun toDisasterGroupList(
        disasterWarningList: List<Pair<Disaster, List<WarningStatus>>>,
    ): List<DisasterGroup> {
        return disasterWarningList.map {
            DisasterGroup(it.first, it.second)
        }
    }

    private fun Long.toTimeString(): TimeString = Util.getStandardTimeString(this)
    private fun String.toTimeString(pattern: String = Const.DB_TIME_PATTERN): TimeString = TimeString(this, pattern)
    fun String.remoteToDbTimeFormat(): TimeString {
        val remSdf = SimpleDateFormat(Const.REMOTE_TIME_PATTERN, Locale.ROOT)
        val dbSdf = SimpleDateFormat(Const.DB_TIME_PATTERN, Locale.ROOT)
        val time = remSdf.parse(this)!!
        val timeStr = dbSdf.format(time)
        return TimeString(timeStr, Const.DB_TIME_PATTERN)
    }

    fun DisasterEntity.toModel(): Disaster = Disaster(id, name, imgLink)
    fun EmergencyEntity.toModel(): Emergency = Emergency(id, name, color, severity)
    fun LocationEntity.toModel(): Location = Location(id, name, Coordinate(latitude, longitude))
    fun NewsEntity.toModel(): News = News(timestamp.toTimeString(), title, briefDesc, linkImage, link, type)
    fun ReportEntity.toModel(location: Location, form: Form?): Report = Report(timestamp.toTimeString(), method, location, form)
    fun ReportEntity.toModelDetail(location: Location, form: Form?): ReportDetail = ReportDetail(toModel(location, form), response)
    fun FormEntity.toModel(): Form = Form(timestamp.toTimeString(), title, desc, photoLinkList.split(Const.CHAR_LINK_SEPARATOR))
    fun UserEntity.toModel(location: Location): User = User(email, name, gender, location)
    fun WarningEntity.toModel(disaster: Disaster, emergency: Emergency, location: Location): WarningStatus = WarningStatus(
        disaster, emergency, title, timestamp.toTimeString(), location, imgLink
    )
    fun WarningEntity.toModelDetail(disaster: Disaster, emergency: Emergency, location: Location, relatedNews: News): WarningDetail = WarningDetail(
        status = toModel(disaster, emergency, location),
        desc = desc,
        relatedNews = relatedNews,
    )
    fun WeatherEntity.toModel(): Weather = Weather(id, name, icon)
    fun WeatherForecastEntity.toModel(weather: Weather): WeatherForecast = WeatherForecast(
        weather, temperature, humidity, rainfall, windSpeed, ultraviolet, timestamp.toTimeString()
    )

    fun Disaster.toEntity(): DisasterEntity = DisasterEntity(id, name, imgLink)
    fun Emergency.toEntity(): EmergencyEntity = EmergencyEntity(id, name, color, severity)
    fun Location.toEntity(parentId: Int): LocationEntity = LocationEntity(id, name, coordinate.latitude, coordinate.longitude, parentId)
    fun News.toEntity(): NewsEntity = NewsEntity(timestamp.timeLong, title, briefDesc, linkImage, link, type)
    fun ReportDetail.toEntity(userId: Int): ReportEntity = ReportEntity(report.timestamp.timeLong, report.method, response, report.location.id, userId)
    fun Form.toEntity(userId: Int): FormEntity = FormEntity(timestamp.timeLong, title, desc, photoLinkList.joinToString(Const.CHAR_LINK_SEPARATOR.toString()), userId)
    fun User.toEntity(id: Int = 0): UserEntity = UserEntity(id, email, name, gender, location.id)
    fun WarningDetail.toEntity(): WarningEntity = WarningEntity(
        timestamp = status.timestamp.timeLong,
        locationId = status.location.id,
        disasterId = status.disaster.id,
        emergencyId = status.emergency.id,
        title = status.title,
        desc = desc,
        imgLink = status.imgLink,
        relatedNewsTimestamp = relatedNews.timestamp.time
    )
    fun Weather.toEntity(): WeatherEntity = WeatherEntity(id, name, icon)
    fun WeatherForecast.toEntity(locationId: Int): WeatherForecastEntity = WeatherForecastEntity(
        timestamp = timestamp.timeLong,
        locationId = locationId,
        temperature = temperature,
        humidity = humidity,
        rainfall = rainfall,
        windSpeed = windSpeed,
        ultraviolet = ultraviolet,
        weatherId = weather.id,
    )


    fun NewsResponse.toModel(type: Int): News = News(
        timestamp = timestamp.remoteToDbTimeFormat(),
        title = title,
        briefDesc = desc,
        linkImage = imgLink,
        link = link,
        type = type,
    )

    fun LoginResponse.toModel(): User = User(
        email = data.email,
        name = data.full_name,
        gender = data.gender,
        location = Location(0, "TODO", Coordinate(0.0, 0.0)) //TODO
    )
    fun User.toUpdateReqBody(newPswd: String): UpdateProfileReqBody = UpdateProfileReqBody(
        email, name, location.name, gender, newPswd,
    )

    fun GeneralCityListResponse.toModel(coordinate: Coordinate = Coordinate(0.0, 0.0)): List<Location> = nestedcityList.map {
        val locName = it.cityList.first()
        Location(0, locName, coordinate)
    }

    fun WeatherForecastResponse.toModel(weather: Weather? = null): WeatherForecast = WeatherForecast(
        weather ?: Dummy.getWeatherByName(weatherName), temperature.toFloat(), humidity.toFloat(), rainfall.toFloat(),
        windSpeed.toFloat(), ultraviolet.toFloat(), Util.getTimeString(TimeString(date, Const.DB_TIME_PATTERN)),
    )

    fun LandslideResponse.toModel(location: Location): WarningStatus {
        val disaster = Dummy.getDisasterByName(Const.Disaster.LANDSLIDE)
        val emergency = Dummy.getLandslideEmergencyByName(condition)
        val caption = CaptionMapper.WarningStatus.getCaption(disaster, emergency)

        return WarningStatus(disaster, emergency, caption.title, Util.getTimeString(), location, "")
    }

    fun EarthQuakeResponse.toModel(location: Location): WarningStatus {
        val disaster = Dummy.getDisasterByName(Const.Disaster.EARTH_QUAKE)
        val emergency = Dummy.getEarthQuakeEmergencyByScale(avg_magnitude)
        val caption = CaptionMapper.WarningStatus.getCaption(disaster, emergency)

        return WarningStatus(disaster, emergency, caption.title, date.remoteToDbTimeFormat(), location, "")
    }


    fun FloodResponse.toModel(location: Location): WarningStatus {
        val disaster = Dummy.getDisasterByName(Const.Disaster.FLOOD)
        val emergency = Dummy.getFloodEmergencyByName(condition)
        val caption = CaptionMapper.WarningStatus.getCaption(disaster, emergency)

        return WarningStatus(disaster, emergency, caption.title, Util.getTimeString(), location, "")
    }



    fun AuthData.toLoginBody(): LoginBody = LoginBody(
        _email = email,
        _password = password,
    )

    fun User.toSignupData(password: String): SignupBody = SignupBody(
        _email = email,
        _password = password,
        _fname = name,
        _gender = gender,
    )

    fun Form.toReportReqBody(email: String): ReportReqBody = ReportReqBody(email, desc, photoLinkList.firstOrNull() ?: "")

    fun getGenderName(char: Char): String = when(char) {
        Const.GENDER_MALE -> "Pria"
        Const.GENDER_FEMALE -> "Wanita"
        else -> throw IllegalArgumentException("No such gender ($char)")
    }
    fun getMethodName(method: Int): String = when(method) {
        Const.METHOD_CALL -> "Telepon"
        Const.METHOD_FORM -> "Form"
        else -> throw IllegalArgumentException("No such method ($method)")
    }

    //fun convertRemoteTimestampToLocalFormat(remoteTimestamp: String): String = remoteTimestamp.split(" ")[0]


    fun <T> Result<T>.toListResult(): Result<List<T>> = when(this){
        is Success -> Success(listOf(data), 0)
        is Fail -> this
    }
    fun <T> Result<List<T>>.toSingleResult(): Result<T> = when(this){
        is Success -> data.firstOrNull()?.let { Success(it, 0) } ?: Util.noEntityFailResult()
        is Fail -> this
    }

    fun Result<List<WarningDetail>>.toWarningStatusListResult(): Result<List<WarningStatus>> = when(this){
        is Success -> Success(data.map { it.status }, 0)
        is Fail -> this
    }
    fun Result<List<WarningStatus>>.toWarningDetailListResult(): Result<List<WarningDetail>> = when(this){
        is Success -> Success(data.map { WarningDetail(it, "", Dummy.emptyNews) }, 0)
        is Fail -> this
    }
    fun Result<List<ReportDetail>>.toReportListResult(): Result<List<Report>> = when(this){
        is Success -> Success(data.map { it.report }, 0)
        is Fail -> this
    }


    suspend fun <I, O> Call<List<I>>.toListResult(mapper: (I) -> O): Result<List<O>> {
        return try {
            val resp = execute()
            if (resp.isSuccessful) {
                val bodyList = resp.body()!!
                val modelList = mutableListOf<O>()
                for (body in bodyList) {
                    val model = mapper(body)
                    modelList += model
                }
                Success(modelList, 0)
            } else {
                val code = resp.code()
                val msg = resp.message()
                Fail(msg, code, null)
            }
        } catch (e: Throwable) {
            Fail(e.message, -1, e)
        }
    }
}