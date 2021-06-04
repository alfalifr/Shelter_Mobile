package sidev.app.bangkit.capstone.sheltermobile.core.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sidev.app.bangkit.capstone.sheltermobile.core.data.entity.WeatherEntity

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather")
    fun getAllWeathers(): List<WeatherEntity>

    @Query("SELECT * FROM weather WHERE id = :id")
    fun getWeather(id: Int): WeatherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveWeather(data: WeatherEntity): Long

    fun saveWeatherList(list: List<WeatherEntity>): Int {
        var inserted = 0
        for(e in list) {
            if(saveWeather(e) >= 0L)
                inserted++
        }
        return inserted
    }
}