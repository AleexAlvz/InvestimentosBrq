package br.com.alexalves.investimentosbrq.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InvestimentosServiceAPI {

    fun getInvestimentosService(): InvestimentosService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.hgbrasil.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(InvestimentosService::class.java)

    }
}