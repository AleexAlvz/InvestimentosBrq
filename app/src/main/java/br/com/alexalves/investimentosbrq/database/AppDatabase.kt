package br.com.alexalves.investimentosbrq.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.alexalves.investimentosbrq.model.Usuario

@Database(entities = [Usuario::class], version=2, exportSchema = false)
@TypeConverters(BigDecimalConverter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract val usuarioDao: UsuarioDao
}