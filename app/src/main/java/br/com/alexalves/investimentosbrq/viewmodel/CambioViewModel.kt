package br.com.alexalves.investimentosbrq.viewmodel

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.alexalves.investimentosbrq.R
import br.com.alexalves.investimentosbrq.database.UsuarioDao
import br.com.alexalves.investimentosbrq.model.Moeda
import br.com.alexalves.investimentosbrq.repository.MoedasRepository
import java.math.BigDecimal
import java.math.BigInteger

class CambioViewModel(
    val moedasRepository: MoedasRepository
) : ViewModel() {

    val saldoUsuario = MutableLiveData<BigDecimal>()
    val moedasEmCaixa = MutableLiveData<BigInteger>()

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

    fun buscaCorMoeda(variacao: Double, context: Context): LiveData<Int> {
        val cor = if (variacao > 0) {
            ContextCompat.getColor(context, R.color.green_positive)
        } else if (variacao < 0) {
            ContextCompat.getColor(context, R.color.red_negative)
        } else ContextCompat.getColor(context, R.color.white)
        val mutableLiveDataCor = MutableLiveData<Int>(cor)
        return mutableLiveDataCor
    }

    fun compraMoeda(
        moeda: Moeda,
        quantidade: BigInteger,
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
        quantidade: BigInteger,
        quandoSucesso: ((totalDaVenda: BigDecimal) -> Unit)?,
        quandoFalha: ((erro: String) -> Unit)?
    ) {
        moedasRepository.vendeMoeda(moeda, quantidade,
            quandoSucesso = { totalDaVenda -> quandoSucesso?.invoke(totalDaVenda) },
            quandoFalha = { erro -> quandoFalha?.invoke(erro) }
        )
    }

}
