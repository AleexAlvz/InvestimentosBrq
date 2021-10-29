package br.com.alexalves.base.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
abstract class UserDAO {

    @Update
    abstract fun updateUser(user: br.com.alexalves.models.User)

    @Query("SELECT * FROM User WHERE id == :id")
    abstract fun searchUser(id: Long): br.com.alexalves.models.User

    @Insert
    abstract fun saveUser(user: br.com.alexalves.models.User)
}
