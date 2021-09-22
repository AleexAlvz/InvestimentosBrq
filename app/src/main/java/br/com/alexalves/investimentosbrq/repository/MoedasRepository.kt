package br.com.alexalves.investimentosbrq.repository

import br.com.alexalves.investimentosbrq.model.Moeda
import br.com.alexalves.investimentosbrq.model.ServiceInvestimentos
import br.com.alexalves.investimentosbrq.retrofit.InvestimentosServiceAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class MoedasRepository(
) {


    val service = InvestimentosServiceAPI().getInvestimentosService()

    fun buscaMoedas(
        quandoSucesso: ((moedas: List<Moeda>) -> Unit)? = null,
        quandoFalha: ((erro: String) -> Unit)? = null
    ) {
        val callService = service.getService()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val service = callService.execute().body()
                val moedasBuscadas = buscaMoedas(service)
                withContext(Main) {
                    quandoSucesso?.invoke(moedasBuscadas)
                }

            } catch (erro: IOException) {
                quandoFalha?.invoke(erro.message.toString())
            }
        }
    }

    private fun buscaMoedas(service: ServiceInvestimentos?): List<Moeda> {
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
            val moedasDaFuncao = listOf(ars, aud, btc, cad, cny, eur, gbp, jpy, usd)
            moedas = moedasDaFuncao
        }
        return moedas as List<Moeda>
    }
}