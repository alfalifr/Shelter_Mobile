package sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.LocationRepo
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result

interface LocationLocalSource: LocationRepo {
    suspend fun getCurrentLocation(): Result<Location>
    suspend fun saveCurrentLocation(data: Location): Result<Boolean>
    suspend fun saveLocation(data: Location): Result<Boolean>
    suspend fun getLocationByName(name: String): Result<Location>
}