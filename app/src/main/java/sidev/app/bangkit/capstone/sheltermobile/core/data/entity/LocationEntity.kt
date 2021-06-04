package sidev.app.bangkit.capstone.sheltermobile.core.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "location", indices = [Index("name")])
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val parentId: Int, // if null (doesn't have parent), -1
)