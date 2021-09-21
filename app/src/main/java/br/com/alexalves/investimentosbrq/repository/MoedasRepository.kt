package br.com.alexalves.investimentosbrq.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import br.com.alexalves.investimentosbrq.model.Moeda
import br.com.alexalves.investimentosbrq.model.ServiceInvestimentos
import br.com.alexalves.investimentosbrq.retrofit.InvestimentosServiceAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoedasRepository {

    val service = InvestimentosServiceAPI().getInvestimentosService()
    val moedas = MutableLiveData<List<Moeda>>()

    fun buscaMoedas(){
        val callService = service.getService()
        CoroutineScope(Dispatchers.IO).launch {
            val service = callService.execute().body()
            val moedasBuscadas = buscaMoedas(service)
            withContext(Main){
                moedas.value = moedasBuscadas
            }
        }
    }

    private fun buscaMoedas(service: ServiceInvestimentos?): List<Moeda> {
        if (service?.results?.currencies!=null){
            service.results.currencies.let {
                val ars = it.ars
                ars.setAbreviacao()
                val aud = it.aud
                aud.setAbreviacao()
                val btc = it.btc
                btc.setAbreviacao()
                val cad = it.cad
                cad.setAbreviacao()
                val cny = it.cny
                cny.setAbreviacao()
                val eur = it.eur
                eur.setAbreviacao()
                val gbp = it.gbp
                gbp.setAbreviacao()
                val jpy = it.jpy
                jpy.setAbreviacao()
                val usd = it.usd
                usd.setAbreviacao()
                val moedas = listOf(ars, aud, btc, cad, cny, eur, gbp, jpy, usd)
                return moedas
            }
        }else return listOf()
    }
}