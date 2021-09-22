package br.com.alexalves.investimentosbrq.repository

import android.content.Context
import br.com.alexalves.investimentosbrq.database.DatabaseBuilder
import br.com.alexalves.investimentosbrq.database.UsuarioDao
import br.com.alexalves.investimentosbrq.model.Moeda
import br.com.alexalves.investimentosbrq.model.ServiceInvestimentos
import br.com.alexalves.investimentosbrq.model.Usuario
import br.com.alexalves.investimentosbrq.retrofit.InvestimentosServiceAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.Exception
import java.math.BigDecimal

class MoedasRepository(
    val usuarioDao: UsuarioDao
) {

    val service = InvestimentosServiceAPI().getInvestimentosService()

    fun buscaSaldo(
        quandoSucesso: ((saldo: BigDecimal)->Unit)? = null,
        quandoFalha: ((erro: String) -> Unit)? = null
    ){
        try {
            CoroutineScope(IO).launch {
                val usuario = usuarioDao.buscaUsuario(id = 1)
                withContext(Main){
                    if (usuario!=null){
                        quandoSucesso?.invoke(usuario.saldo)
                    } else quandoFalha?.invoke("Usuario não encontrado")
                }
            }
        } catch (erro: Exception){
            quandoFalha?.invoke(erro.message.toString())
        }

    }

    fun configuraeFiltraMoedas(
        quandoSucesso: ((moedas: List<Moeda>) -> Unit)? = null,
        quandoFalha: ((erro: String) -> Unit)? = null
    ) {
        val callService = service.getService()
        CoroutineScope(IO).launch {
            try {
                val service = callService.execute().body()
                val moedasBuscadas = configuraeFiltraMoedas(service)
                withContext(Main) {
                    quandoSucesso?.invoke(moedasBuscadas)
                }
            } catch (erro: Exception) {
                quandoFalha?.invoke(erro.message.toString())
            }
        }
    }

    private fun configuraeFiltraMoedas(service: ServiceInvestimentos?): List<Moeda> {
        var moedas: List<Moeda>? = null
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
            val moedasSemNull = arrayListOf<Moeda>()
            for (moeda in moedasDaFuncao){
                if (moeda.sell!=null&&moeda.buy!=null){
                    moedasSemNull.add(moeda)
                }
            }
            moedas = moedasSemNull
        }
        return moedas as List<Moeda>
    }

    fun buscaSaldoEmCaixa(
        quandoSucesso: ((saldoEmCaixa: Int) -> Unit)?,
        quandoFalha: ((erro: String) -> Unit)?
    ) {

    }
}