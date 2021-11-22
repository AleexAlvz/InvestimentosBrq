package br.com.alexalves.base.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object InvestimentoServiceAPI {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.hgbrasil.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T : Any?> provideApi(apiInterface: Class<T>): T = retrofit.create(apiInterface)

}