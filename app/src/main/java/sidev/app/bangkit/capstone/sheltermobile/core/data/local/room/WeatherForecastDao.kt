package sidev.app.bangkit.capstone.sheltermobile.core.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sidev.app.bangkit.capstone.sheltermobile.core.data.entity.WeatherForecastEntity

@Dao
interface WeatherForecastDao {
    @Query("SELECT * FROM weather_forecast WHERE timestamp = :timestamp AND locationId = :locationId")
    fun getWeatherForecast(timestamp: Long, locationId: Int): WeatherForecastEntity?

    @Query("SELECT * FROM weather_forecast WHERE locationId = :locationId AND timestamp >= :startTimestamp")
    fun getWeatherForecastBatch(startTimestamp: Long, locationId: Int): List<WeatherForecastEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveWeatherForecast(data: WeatherForecastEntity): Long

    fun saveWeatherForecastList(list: List<WeatherForecastEntity>): Int {
        var inserted = 0
        for(e in list){
            if(saveWeatherForecast(e) >= 0)
                inserted++
        }
        return inserted
    }
}