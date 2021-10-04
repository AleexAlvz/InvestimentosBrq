package br.com.alexalves.investimentosbrq.repository

import br.com.alexalves.investimentosbrq.database.UsuarioDao
import br.com.alexalves.investimentosbrq.model.Moeda
import br.com.alexalves.investimentosbrq.model.ServiceInvestimentos
import br.com.alexalves.investimentosbrq.model.Usuario
import br.com.alexalves.investimentosbrq.retrofit.InvestimentosServiceAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.BigInteger

class MoedasRepository(
    private val usuarioDao: UsuarioDao
) {
    private val service = InvestimentosServiceAPI().getInvestimentosService()

    fun buscaUsuario(
        quandoSucesso: ((usuario: Usuario) -> Unit)? = null,
        quandoFalha: ((erro: String) -> Unit)? = null
    ) {
        try {
            CoroutineScope(IO).launch {
                val usuario = usuarioDao.buscaUsuario(id = 1)
                withContext(Main) {
                    if (usuario != null) {
                        quandoSucesso?.invoke(usuario)
                    } else quandoFalha?.invoke("Usuario não encontrado")
                }
            }
        } catch (erro: Exception) {
            quandoFalha?.invoke(erro.message.toString())
        }
    }

    fun buscaMoedas(
        quandoSucesso: ((moedas: List<Moeda>) -> Unit)? = null,
        quandoFalha: ((erro: String) -> Unit)? = null
    ) {
        val callService = service.getService()
        CoroutineScope(IO).launch {
            try {
                val service = callService.execute().body()
                val moedasBuscadas = configuraEFiltraMoedas(service)
                withContext(Main) {
                    quandoSucesso?.invoke(moedasBuscadas)
                }
            } catch (erro: Exception) {
                quandoFalha?.invoke(erro.message.toString())
            }
        }
    }

    private fun configuraEFiltraMoedas(service: ServiceInvestimentos?): List<Moeda> {
        var moedas = arrayListOf<Moeda>()
        service?.results?.currencies?.let {
            val ars = it.ars
            ars.configura(it.source)
            val aud = it.aud
            aud.configura(it.source)
            val btc = it.btc
            btc.configura(it.source)
            val cad = it.cad
            cad.configura(it.source)
            val cny = it.cny
            cny.configura(it.source)
            val eur = it.eur
            eur.configura(it.source)
            val gbp = it.gbp
            gbp.configura(it.source)
            val jpy = it.jpy
            jpy.configura(it.source)
            val usd = it.usd
            usd.configura(it.source)
            val moedasDaFuncao = listOf<Moeda>(ars, aud, btc, cad, cny, eur, gbp, jpy, usd)
            for (moeda in moedasDaFuncao) {
                if (moeda.sell != null && moeda.buy != null) {
                    moedas.add(moeda)
                }
            }
        }
        return moedas
    }

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

    fun compraMoeda(
        moeda: Moeda,
        quantidade: BigInteger,
        quandoSucesso: ((totalDaCompra: BigDecimal) -> Unit)?,
        quandoFalha: ((erro: String) -> Unit)?
    ) {
        try {
            CoroutineScope(IO).launch {
                val usuario = usuarioDao.buscaUsuario(id = 1)
                val saldoAtual = usuario.saldo
                val moedaEmCaixa = buscaMoedaPelaAbreviacao(moeda, usuario)
                val totalDaCompra = quantidade.toBigDecimal() * moeda.buy
                if (quantidade != BigInteger.ZERO) {
                    val aprovacao = (totalDaCompra <= saldoAtual)
                    if (aprovacao) {
                        val saldoFinal = saldoAtual - totalDaCompra
                        val moedasEmCaixaFinal = moedaEmCaixa + quantidade
                        salvaCompraNoUsuario(saldoFinal, moedasEmCaixaFinal, moeda, usuario)
                        usuarioDao.atualizaUsuario(usuario)
                        quandoSucesso?.invoke(totalDaCompra)
                    }
                } else {
                    quandoFalha?.invoke("O pedido de compra deve ser maior que 0 unidades")
                }
            }
        } catch (erro: Exception) {
            quandoFalha?.invoke(erro.message.toString())
        }
    }

    fun vendeMoeda(
        moeda: Moeda,
        quantidade: BigInteger,
        quandoSucesso: ((totalDaVenda: BigDecimal) -> Unit)?,
        quandoFalha: ((erro: String) -> Unit)?
    ) {
        try {
            CoroutineScope(IO).launch {
                val usuario = usuarioDao.buscaUsuario(id = 1)
                val saldoAtual = usuario.saldo
                val moedaEmCaixa = buscaMoedaPelaAbreviacao(moeda, usuario)
                val totalDaVenda = quantidade.toBigDecimal() * moeda.sell
                if (quantidade != BigInteger.ZERO) {
                    val aprovacao = (quantidade <= moedaEmCaixa)
                    if (aprovacao) {
                        val saldoFinal = saldoAtual + totalDaVenda
                        val moedasEmCaixaFinal = moedaEmCaixa - quantidade
                        salvaCompraNoUsuario(saldoFinal, moedasEmCaixaFinal, moeda, usuario)
                        usuarioDao.atualizaUsuario(usuario)
                        quandoSucesso?.invoke(totalDaVenda)
                    }
                } else {
                    quandoFalha?.invoke("O pedido de venda deve ser maior que 0 unidades")
                }
            }
        } catch (erro: Exception) {
            quandoFalha?.invoke(erro.message.toString())
        }
    }

    private fun salvaCompraNoUsuario(
        saldoFinal: BigDecimal,
        moedasEmCaixaFinal: BigInteger,
        moeda: Moeda,
        usuario: Usuario
    ) {
        usuario.saldo = saldoFinal
        when (moeda.abreviacao) {
            "USD" -> usuario.usd = moedasEmCaixaFinal
            "EUR" -> usuario.eur = moedasEmCaixaFinal
            "GBP" -> usuario.gbp = moedasEmCaixaFinal
            "ARS" -> usuario.ars = moedasEmCaixaFinal
            "CAD" -> usuario.cad = moedasEmCaixaFinal
            "AUD" -> usuario.aud = moedasEmCaixaFinal
            "JPY" -> usuario.jpy = moedasEmCaixaFinal
            "CNY" -> usuario.cny = moedasEmCaixaFinal
            "BTC" -> usuario.btc = moedasEmCaixaFinal
        }
    }
}