package sidev.app.bangkit.capstone.sheltermobile.data.repo

import android.location.Location
import sidev.app.bangkit.capstone.sheltermobile.data.model.Coordinate

interface LocationRepo {
    /**
     * [locationName] can be either city, province, or other particular place name.
     */
    suspend fun getPresetLocationList(locationName: String): List<Location>
    suspend fun getPresetLocationList(locationCoordinate: Coordinate): List<Location>
    suspend fun getLocation(coordinate: Coordinate): Location
}