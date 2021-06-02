package sidev.app.bangkit.capstone.sheltermobile.core.util

import retrofit2.Call
import sidev.app.bangkit.capstone.sheltermobile.core.data.entity.*
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.LoginBody
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.SignupBody
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.LoginResponse
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.response.NewsResponse
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.*
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.presentation.model.DisasterGroup
import java.lang.IllegalArgumentException

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

    private fun String.toTimeString(pattern: String = Const.DB_TIME_PATTERN): TimeString = TimeString(this, pattern)
    private fun String.remoteToDbTimeFormat(): TimeString {

    }

    fun DisasterEntity.toModel(): Disaster = Disaster(id, name, imgLink)
    fun EmergencyEntity.toModel(): Emergency = Emergency(id, name, color, severity)
    fun LocationEntity.toModel(): Location = Location(id, name, Coordinate(latitude, longitude))
    fun NewsEntity.toModel(): News = News(timestamp.toTimeString(), title, briefDesc, linkImage, link, type)
    fun ReportEntity.toModel(location: Location, form: Form): Report = Report(timestamp.toTimeString(), method, location, form)
    fun ReportEntity.toModelDetail(location: Location, form: Form): ReportDetail = ReportDetail(toModel(location, form), response)
    fun FormEntity.toModel(): Form = Form(timestamp.toTimeString(), title, desc, photoLinkList.split(Const.CHAR_LINK_SEPARATOR))
    fun UserEntity.toModel(): User = User(email, name, gender)
    fun WarningEntity.toModel(disaster: Disaster, emergency: Emergency, location: Location): WarningStatus = WarningStatus(
        disaster, emergency, title, timestamp.toTimeString(), location, imgLink
    )
    fun WarningEntity.toModelDetail(disaster: Disaster, emergency: Emergency, location: Location, relatedNews: News): WarningDetail = WarningDetail(
        status = toModel(disaster, emergency, location),
        desc = desc,
        relatedNews = relatedNews,
    )
    fun WeatherEntity.toModel(): WeatherForecast = WeatherForecast(
        temperature, humidity, rainfall, windSpeed, ultraviolet, timestamp.toTimeString()
    )

    fun Disaster.toEntity(): DisasterEntity = DisasterEntity(id, name, imgLink)
    fun Emergency.toEntity(): EmergencyEntity = EmergencyEntity(id, name, color, severity)
    fun Location.toEntity(parentId: Int): LocationEntity = LocationEntity(id, name, coordinate.latitude, coordinate.longitude, parentId)
    fun News.toEntity(): NewsEntity = NewsEntity(timestamp.time, title, briefDesc, linkImage, link, type)
    fun ReportDetail.toEntity(): ReportEntity = ReportEntity(report.timestamp.time, report.method, response, report.location.id)
    fun Form.toEntity(): FormEntity = FormEntity(timestamp.time, title, desc, photoLinkList.joinToString(Const.CHAR_LINK_SEPARATOR.toString()))
    fun User.toEntity(): UserEntity = UserEntity(email, name, gender)
    fun WarningDetail.toEntity(): WarningEntity = WarningEntity(
        timestamp = status.timestamp.time,
        locationId = status.location.id,
        disasterId = status.disaster.id,
        emergencyId = status.emergency.id,
        title = status.title,
        desc = desc,
        imgLink = status.imgLink,
        relatedNewsTimestamp = relatedNews.timestamp.time
    )
    fun WeatherForecast.toEntity(locationId: Int): WeatherEntity = WeatherEntity(
        timestamp = timestamp.time,
        locationId = locationId,
        temperature = temperature,
        humidity = humidity,
        rainfall = rainfall,
        windSpeed = windSpeed,
        ultraviolet = ultraviolet
    )


    fun NewsResponse.toModel(type: Int): News = News(
        timestamp = timestamp.toTimeString(),
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
    )

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