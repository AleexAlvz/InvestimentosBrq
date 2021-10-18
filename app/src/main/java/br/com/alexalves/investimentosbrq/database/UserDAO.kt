package br.com.alexalves.investimentosbrq.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.alexalves.investimentosbrq.model.User

@Dao
abstract class UserDAO {

    @Update
    abstract fun updateUser(user: User)

    @Query("SELECT * FROM User WHERE id == :id")
    abstract fun searchUser(id: Long): User

    @Insert
    abstract fun addUser(user: User)
}
