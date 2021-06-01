package sidev.app.bangkit.capstone.sheltermobile.core.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sidev.app.bangkit.capstone.sheltermobile.core.data.entity.WeatherEntity

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather WHERE timestamp = :timestamp AND locationId = :locationId")
    fun getWeatherForecast(timestamp: String, locationId: Int): WeatherEntity?

    @Query("SELECT * FROM weather WHERE locationId = :locationId AND timestamp >= :startTimestamp")
    fun getWeatherForecastBatch(startTimestamp: String, locationId: Int): List<WeatherEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveWeatherForecast(data: WeatherEntity): Long

    fun saveWeatherForecastList(list: List<WeatherEntity>): Int {
        var inserted = 0
        for(e in list){
            if(saveWeatherForecast(e) >= 0)
                inserted++
        }
        return inserted
    }
}