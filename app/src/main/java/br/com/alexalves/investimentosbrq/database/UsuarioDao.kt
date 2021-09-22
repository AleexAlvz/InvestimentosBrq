package br.com.alexalves.investimentosbrq.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.alexalves.investimentosbrq.model.Usuario

@Dao
abstract class UsuarioDao {

    @Update
    abstract fun atualizaUsuario(usuario: Usuario)

    @Query("SELECT * FROM Usuario WHERE id == :id")
    abstract fun buscaUsuario(id: Long): Usuario

    @Insert
    abstract fun adicionaUsuario(usuario: Usuario)
}
