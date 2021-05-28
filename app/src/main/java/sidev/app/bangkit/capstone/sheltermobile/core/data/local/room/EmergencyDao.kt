package sidev.app.bangkit.capstone.sheltermobile.core.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import sidev.app.bangkit.capstone.sheltermobile.core.data.entity.EmergencyEntity

@Dao
interface EmergencyDao {
    @Query("SELECT * FROM emergency")
    fun getAllEmergencies(): List<EmergencyEntity>

    @Query("SELECT * FROM emergency WHERE id = :id")
    fun getEmergency(id: Int): EmergencyEntity?

    @Insert
    fun saveEmergencyList(list: List<EmergencyEntity>): Int
}