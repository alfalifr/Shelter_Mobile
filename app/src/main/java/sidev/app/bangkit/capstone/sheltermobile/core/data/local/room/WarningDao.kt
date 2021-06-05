package sidev.app.bangkit.capstone.sheltermobile.core.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sidev.app.bangkit.capstone.sheltermobile.core.data.entity.WarningEntity

@Dao
interface WarningDao {
    @Query("SELECT * FROM warning WHERE disasterId = :disasterId AND locationId = :locationId AND timestamp >= :startTimestamp")
    fun getWarningStatusBatch(disasterId: Int, locationId: Int, startTimestamp: Long): List<WarningEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveWarning(data: WarningEntity): Long

    fun saveWarningList(list: List<WarningEntity>): Int {
        var inserted = 0
        for(e in list) {
            if(saveWarning(e) >= 0)
                inserted++
        }
        return inserted
    }
}