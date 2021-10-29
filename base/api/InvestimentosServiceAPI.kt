package br.com.alexalves.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object InvestimentosServiceAPI {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.hgbrasil.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T: Any?>provideApi(apiInterface: Class<T>):T = retrofit.create(apiInterface)

}

//Home
//Cambio
//base (API, BD, BASE)
//Utils
//Models
//Extrair versoes das libs para o gradle appGeral