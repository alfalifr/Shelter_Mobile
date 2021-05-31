package sidev.app.bangkit.capstone.sheltermobile.core.data.local.room

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sidev.app.bangkit.capstone.sheltermobile.core.data.entity.FormEntity

interface FormDao {
    @Query("SELECT * FROM form WHERE id = :id")
    fun getForm(id: Int): FormEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveForm(data: FormEntity): Int
}