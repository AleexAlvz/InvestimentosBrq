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

    fun atualizaSaldo() {
        moedasRepository.buscaSaldo(
            quandoFalha = { erro -> Log.i("BuscaSaldo", "Busca do saldo falhou: ${erro}") },
            quandoSucesso = { saldo -> saldoUsuario.value = saldo }
        )
    }

    fun atualizaSaldoEmCaixa(moeda: Moeda) {
        moedasRepository.buscaMoedaEmCaixa(
            moeda,
            quandoSucesso = { saldoEmCaixa -> moedasEmCaixa.value = saldoEmCaixa },
            quandoFalha = { erro -> Log.i("BuscaSaldoEmCaixa", "Busca do saldo falhou: ${erro}") }
        )
    }

    fun compraMoeda(
        moeda: Moeda,
        quantidade: Int,
        quandoSucesso: ((totalDaCompra: BigDecimal) -> Unit)?,
        quandoFalha: ((erro: String) -> Unit)?

    ) {
        moedasRepository.compraMoeda(moeda, quantidade,
            quandoSucesso = { totalDaCompra -> quandoSucesso?.invoke(totalDaCompra) },
            quandoFalha = { erro -> quandoFalha?.invoke(erro) }
        )
    }

    fun vendeMoeda(
        moeda: Moeda,
        quantidade: Int,
        quandoSucesso: ((totalDaVenda: BigDecimal) -> Unit)?,
        quandoFalha: ((erro: String) -> Unit)?
    ) {
        moedasRepository.vendeMoeda(moeda, quantidade,
            quandoSucesso = { totalDaVenda -> quandoSucesso?.invoke(totalDaVenda) },
            quandoFalha = { erro -> quandoFalha?.invoke(erro) }
        )
    }

}
