package br.com.alexalves.investimentosbrq.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.alexalves.investimentosbrq.database.UsuarioDao
import br.com.alexalves.investimentosbrq.model.Moeda
import br.com.alexalves.investimentosbrq.repository.MoedasRepository
import java.math.BigDecimal

class CambioViewModel(
    private val usuarioDao: UsuarioDao
    ) : ViewModel() {

    val moedasRepository = MoedasRepository(usuarioDao)

    val saldoUsuario = MutableLiveData<BigDecimal>()
    var moedasEmCaixa = MutableLiveData<Int>()

    fun atualizaSaldo(){
        moedasRepository.buscaSaldo(
            quandoFalha = { erro -> Log.i("BuscaSaldo","Busca do saldo falhou: ${erro}") },
            quandoSucesso = { saldo -> saldoUsuario.value = saldo }
        )
    }

    fun atualizaSaldoEmCaixa(moeda: Moeda){
        moedasRepository.buscaSaldoEmCaixa(
            quandoSucesso = { saldoEmCaixa -> moedasEmCaixa.value = saldoEmCaixa },
            quandoFalha = { erro -> Log.i("BuscaSaldoEmCaixa","Busca do saldo falhou: ${erro}")}
        )
    }

    fun compraMoeda(moeda: Moeda, quantidade: Int){}

    fun vendeMoeda(moeda: Moeda, quantidade: Int){}

}
