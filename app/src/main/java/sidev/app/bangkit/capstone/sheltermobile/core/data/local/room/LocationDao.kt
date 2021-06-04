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

    @Query("SELECT * FROM location WHERE name = :name COLLATE NOCASE")
    fun getLocationByName(name: String): LocationEntity?

    @Query("SELECT * FROM location WHERE id = :id")
    fun getLocationById(id: Int): LocationEntity?

    fun getLocation(coordinate: Coordinate): LocationEntity? = getLocation(coordinate.latitude, coordinate.longitude)

    @Insert(onConflict = OnConflictStrategy.IGNORE) //It could be duplicate because locations are gathered from each disaster from server.
    fun saveLocation(data: LocationEntity): Long

    fun saveLocationList(list: List<LocationEntity>): Int {
        var inserted = 0
        for(e in list){
            if(saveLocation(e) >= 0)
                inserted++
        }
        return inserted
    }
}