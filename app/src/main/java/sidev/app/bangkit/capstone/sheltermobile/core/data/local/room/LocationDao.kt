package sidev.app.bangkit.capstone.sheltermobile.core.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sidev.app.bangkit.capstone.sheltermobile.core.data.entity.LocationEntity
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Coordinate

@Dao
interface LocationDao {
    @Query("SELECT * FROM location")
    fun getAllLocation(): List<LocationEntity>

    @Query("SELECT * FROM location WHERE latitude = :latitude AND longitude = :longitude")
    fun getLocation(latitude: Double, longitude: Double): LocationEntity?

    @Query("SELECT * FROM location WHERE id = :id")
    fun getLocationById(id: Int): LocationEntity?

    fun getLocation(coordinate: Coordinate): LocationEntity? = getLocation(coordinate.latitude, coordinate.longitude)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveLocationList(list: List<LocationEntity>): Int
}