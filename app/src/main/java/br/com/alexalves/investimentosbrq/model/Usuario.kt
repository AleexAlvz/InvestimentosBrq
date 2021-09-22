package br.com.alexalves.investimentosbrq.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity
class Usuario(
    val saldo: BigDecimal = BigDecimal(1000.0),
    val usd: Int = 0,
    val eur: Int = 0,
    val gbp: Int = 0,
    val ars: Int = 0,
    val cad: Int = 0,
    val aud: Int = 0,
    val jpy: Int = 0,
    val cny: Int = 0,
    val btc: Int = 0,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
){

}
