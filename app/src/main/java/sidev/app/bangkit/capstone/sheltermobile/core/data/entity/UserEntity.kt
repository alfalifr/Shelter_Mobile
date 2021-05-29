package sidev.app.bangkit.capstone.sheltermobile.core.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    val email: String,
    val name: String,
    val gender: Char,
//    val genderId: Int,
//    val gender: Int,
)