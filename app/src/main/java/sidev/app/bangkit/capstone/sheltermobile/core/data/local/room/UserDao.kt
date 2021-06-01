package sidev.app.bangkit.capstone.sheltermobile.core.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sidev.app.bangkit.capstone.sheltermobile.core.data.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE email = :email")
    fun getUser(email: String): UserEntity?

    @Query("SELECT * FROM user")
    fun getUsers(): List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveUser(data: UserEntity): Long
}