package sidev.app.bangkit.capstone.sheltermobile.core.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sidev.app.bangkit.capstone.sheltermobile.core.data.entity.FormEntity

@Dao
interface FormDao {
    @Query("SELECT * FROM form WHERE timestamp = :timestamp")
    fun getForm(timestamp: Long): FormEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveForm(data: FormEntity): Long
}