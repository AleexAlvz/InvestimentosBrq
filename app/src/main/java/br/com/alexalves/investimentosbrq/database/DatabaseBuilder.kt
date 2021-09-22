package br.com.alexalves.investimentosbrq.database

import android.content.Context
import androidx.room.Room

class DatabaseBuilder(val context: Context) {

    fun getDatabase(): AppDatabase{
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "usuario_db")
            .fallbackToDestructiveMigration()
            .build()
    }

}