package sidev.app.bangkit.capstone.sheltermobile.core.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sidev.app.bangkit.capstone.sheltermobile.core.data.entity.EmergencyEntity

@Dao
interface EmergencyDao {
    @Query("SELECT * FROM emergency")
    fun getAllEmergencies(): List<EmergencyEntity>

    @Query("SELECT * FROM emergency WHERE id = :id")
    fun getEmergency(id: Int): EmergencyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveEmergency(data: EmergencyEntity): Long

    fun saveEmergencyList(list: List<EmergencyEntity>): Int {
        var insertedCount = 0
        for(data in list) {
            if(saveEmergency(data) >= 0)
                insertedCount++
        }
        return insertedCount
    }
}