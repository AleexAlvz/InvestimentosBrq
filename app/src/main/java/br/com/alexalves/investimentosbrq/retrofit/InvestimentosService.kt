package br.com.alexalves.investimentosbrq.retrofit

import br.com.alexalves.investimentosbrq.model.ServiceInvestimentos
import retrofit2.Call
import retrofit2.http.GET

interface InvestimentosService {

    @GET("finance?key=b0193f3f")
    fun getService(): Call<ServiceInvestimentos>

}