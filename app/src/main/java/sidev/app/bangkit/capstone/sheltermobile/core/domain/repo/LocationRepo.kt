package sidev.app.bangkit.capstone.sheltermobile.core.domain.repo

import android.location.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Coordinate

interface LocationRepo {
    /**
     * [locationName] can be either city, province, or other particular place name.
     */
    suspend fun getPresetLocationList(parentLocationName: String): Result<List<Location>>
    suspend fun getPresetLocationList(parentLocationCoordinate: Coordinate): Result<List<Location>>
    suspend fun getLocation(coordinate: Coordinate): Result<Location>

    suspend fun saveLocationList(list: List<Location>): Result<Int>
}