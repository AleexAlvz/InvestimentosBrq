package br.com.alexalves.investimentosbrq.viewmodel

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.alexalves.investimentosbrq.R
import br.com.alexalves.investimentosbrq.model.Moeda
import br.com.alexalves.investimentosbrq.model.Usuario
import br.com.alexalves.investimentosbrq.repository.MoedasRepository
import java.math.BigDecimal
import java.math.BigInteger

class CambioViewModel(
    val moedasRepository: MoedasRepository
) : ViewModel() {

    private fun buscaMoedaPelaAbreviacao(moedaBuscada: Moeda, usuario: Usuario) =
        when (moedaBuscada.abreviacao) {
            "USD" -> usuario.usd
            "EUR" -> usuario.eur
            "GBP" -> usuario.gbp
            "ARS" -> usuario.ars
            "CAD" -> usuario.cad
            "AUD" -> usuario.aud
            "JPY" -> usuario.jpy
            "CNY" -> usuario.cny
            "BTC" -> usuario.btc
            else -> throw Exception("Moeda não encontrada")
        }

    fun buscaSaldoEMoedasEmCaixa(
        moeda: Moeda,
        atualizaSaldoEMoedas: (saldo: BigDecimal, moedasEmCaixa: BigInteger) -> Unit
    ) {
        moedasRepository.buscaUsuario(
            quandoFalha = { erro -> Log.i("BuscaSaldo", "Busca do saldo falhou: ${erro}") },
            quandoSucesso = { usuario ->
                run {
                    val saldo = usuario.saldo
                    val moeda = buscaMoedaPelaAbreviacao(moeda, usuario)
                    atualizaSaldoEMoedas.invoke(saldo, moeda)
                }
            })
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
        quandoSucesso: ((totalDaCompra: BigDecimal) -> Unit)?

    ) {
        moedasRepository.compraMoeda(moeda, quantidade,
            quandoSucesso = { totalDaCompra -> quandoSucesso?.invoke(totalDaCompra) },
            quandoFalha = { erro -> Log.e("ERRO COMPRA", erro) }
        )
    }

    fun vendeMoeda(
        moeda: Moeda,
        quantidade: BigInteger,
        quandoSucesso: ((totalDaVenda: BigDecimal) -> Unit)?
    ) {
        moedasRepository.vendeMoeda(moeda, quantidade,
            quandoSucesso = { totalDaVenda -> quandoSucesso?.invoke(totalDaVenda) },
            quandoFalha = { erro -> Log.e("ERRO VENDA", erro) }
        )
    }

}
