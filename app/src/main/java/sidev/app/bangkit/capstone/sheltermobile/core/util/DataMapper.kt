package sidev.app.bangkit.capstone.sheltermobile.core.util

import sidev.app.bangkit.capstone.sheltermobile.core.data.entity.*
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

    fun DisasterEntity.toModel(): Disaster = Disaster(id, name)
    fun EmergencyEntity.toModel(): Emergency = Emergency(id, name, color, severity)
    fun LocationEntity.toModel(): Location = Location(id, name, Coordinate(latitude, longitude))
    fun NewsEntity.toModel(): News = News(timestamp, title, briefDesc, linkImage, link, type)
    fun ReportEntity.toModel(): Report = Report(timestamp, method)
    fun ReportEntity.toModelDetail(): ReportDetail = ReportDetail(toModel(), response)
    fun UserEntity.toModel(): User = User(email, name, gender)
    fun WarningEntity.toModel(disaster: Disaster, emergency: Emergency, location: Location): WarningStatus = WarningStatus(
        disaster, emergency, title, timestamp, location, imgLink
    )
    fun WarningEntity.toModelDetail(disaster: Disaster, emergency: Emergency, location: Location, relatedNews: News): WarningDetail = WarningDetail(
        status = toModel(disaster, emergency, location),
        desc = desc,
        relatedNews = relatedNews,
    )
    fun WeatherEntity.toModel(): WeatherForecast = WeatherForecast(
        temperature, humidity, rainfall, windSpeed, timestamp
    )

    fun Disaster.toEntity(): DisasterEntity = DisasterEntity(id, name)
    fun Emergency.toEntity(): EmergencyEntity = EmergencyEntity(id, name, color, severity)
    fun Location.toEntity(parentId: Int): LocationEntity = LocationEntity(id, name, coordinate.latitude, coordinate.longitude, parentId)
    fun News.toEntity(): NewsEntity = NewsEntity(timestamp, title, briefDesc, linkImage, link, type)
    fun ReportDetail.toEntity(): ReportEntity = ReportEntity(report.timestamp, report.method, response)
    fun User.toEntity(): UserEntity = UserEntity(email, name, gender)
    fun WarningDetail.toEntity(): WarningEntity = WarningEntity(
        timestamp = status.timestamp,
        locationId = status.location.id,
        disasterId = status.disaster.id,
        emergencyId = status.emergency.id,
        title = status.title,
        desc = desc,
        imgLink = status.imgLink,
        relatedNewsTimestamp = relatedNews.timestamp
    )
    fun WeatherForecast.toEntity(locationId: Int): WeatherEntity = WeatherEntity(
        timestamp = timestamp,
        locationId = locationId,
        temperature = temperature,
        humidity = humidity,
        rainfall = rainfall,
        windSpeed = windSpeed,
    )

    fun <T> Result<T>.toListResult(): Result<List<T>> = when(this){
        is Success -> Success(listOf(data), 0)
        is Fail -> this
    }
    fun <T> Result<List<T>>.toSingleResult(): Result<T> = when(this){
        is Success -> Success(data.first(), 0)
        is Fail -> this
    }
}