package br.com.alexalves.investimentosbrq.viewmodel.viewModelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.alexalves.investimentosbrq.database.UsuarioDao
import br.com.alexalves.investimentosbrq.viewmodel.HomeViewModel

class HomeViewModelFactory(val usuarioDao: UsuarioDao): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(usuarioDao) as T
    }
}