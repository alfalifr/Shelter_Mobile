package sidev.app.bangkit.capstone.sheltermobile.core.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import sidev.app.bangkit.capstone.sheltermobile.core.data.entity.*

@Database(
    entities = [
        DisasterEntity::class,
        EmergencyEntity::class,
        LocationEntity::class,
        NewsEntity::class,
        ReportEntity::class,
        FormEntity::class,
        UserEntity::class,
        WarningEntity::class,
        WeatherForecastEntity::class,
        WeatherEntity::class,
    ],
    version = 1,
)
abstract class RoomDb: RoomDatabase() {
    abstract fun getDisasterDao(): DisasterDao
    abstract fun getEmergencyDao(): EmergencyDao
    abstract fun getLocationDao(): LocationDao
    abstract fun getNewsDao(): NewsDao
    abstract fun getReportDao(): ReportDao
    abstract fun getFormDao(): FormDao
    abstract fun getUserDao(): UserDao
    abstract fun getWarningDao(): WarningDao
    abstract fun getWeatherForecastDao(): WeatherForecastDao
    abstract fun getWeatherDao(): WeatherDao

    companion object {
        private var instance: RoomDb?= null
        private var testInstance: RoomDb?= null
        const val DB_NAME = "dbv1"

        fun getInstance(c: Context): RoomDb {
            if(instance == null)
                synchronized(RoomDb::class) {
                    instance = Room.databaseBuilder(c, RoomDb::class.java, DB_NAME).build()
                }
            return instance!!
        }

        fun getInstanceForTest(c: Context): RoomDb {
            if(testInstance == null)
                synchronized(RoomDb::class) {
                    testInstance = Room.inMemoryDatabaseBuilder(c, RoomDb::class.java).build()
                }
            return testInstance!!
        }
    }
}