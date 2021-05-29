package sidev.app.bangkit.capstone.sheltermobile.core.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sidev.app.bangkit.capstone.sheltermobile.core.data.entity.WeatherEntity

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather WHERE timestamp = :timestamp")
    fun getWeatherForecast(timestamp: String): WeatherEntity?

    @Query("SELECT * FROM weather WHERE timestamp >= :startTimestamp")
    fun getWeatherForecastBatch(startTimestamp: String): List<WeatherEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveWeatherForecastList(list: List<WeatherEntity>): Int
}