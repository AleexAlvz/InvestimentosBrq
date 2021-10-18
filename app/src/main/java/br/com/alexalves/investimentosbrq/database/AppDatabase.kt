package br.com.alexalves.investimentosbrq.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.alexalves.investimentosbrq.model.User

@Database(entities = [User::class], version=4, exportSchema = false)
@TypeConverters(BigDecimalConverter::class, BigIntegerConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract val userDAO: UserDAO
}