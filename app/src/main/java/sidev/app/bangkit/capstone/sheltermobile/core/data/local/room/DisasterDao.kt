package sidev.app.bangkit.capstone.sheltermobile.core.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sidev.app.bangkit.capstone.sheltermobile.core.data.entity.DisasterEntity

@Dao
interface DisasterDao {
    @Query("SELECT * FROM disaster")
    fun getDisasterList(): List<DisasterEntity>

    @Query("SELECT * FROM disaster WHERE id = :id")
    fun getDisaster(id: Int): DisasterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveDisaster(data: DisasterEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveDisasterList(list: List<DisasterEntity>): Int {
        var insertedCount = 0
        for(data in list) {
            if(saveDisaster(data) >= 0)
                insertedCount++
        }
        return insertedCount
    }
}