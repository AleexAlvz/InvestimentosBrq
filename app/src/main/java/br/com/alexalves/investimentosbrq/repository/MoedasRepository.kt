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
                val moedas = listOf(it.ars, it.aud, it.btc, it.cad, it.cny, it.eur, it.gbp, it.jpy, it.usd)
                return moedas
            }
        }else return listOf()
    }
}