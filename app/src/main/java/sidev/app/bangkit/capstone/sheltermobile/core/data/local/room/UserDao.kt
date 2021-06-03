package sidev.app.bangkit.capstone.sheltermobile.core.data.local.room

import androidx.room.*
import sidev.app.bangkit.capstone.sheltermobile.core.data.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT COUNT(*) FROM user")
    fun getRowCount(): Int

    @Query("SELECT * FROM user WHERE email = :email")
    fun getUser(email: String): UserEntity?

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUserById(id: Int): UserEntity?

    @Query("SELECT * FROM user")
    fun getUsers(): List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveUser(data: UserEntity): Long

    @Update
    fun updateUser(data: UserEntity): Int
}