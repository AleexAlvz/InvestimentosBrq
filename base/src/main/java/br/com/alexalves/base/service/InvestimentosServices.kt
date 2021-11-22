package br.com.alexalves.base.service

import br.com.alexalves.models.ServiceInvestimentos
import retrofit2.Call
import retrofit2.http.GET

interface InvestimentosService {

    @GET("finance?key=b0193f3f")
    fun getService(): Call<ServiceInvestimentos>

}