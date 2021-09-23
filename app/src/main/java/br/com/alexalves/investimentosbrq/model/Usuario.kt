package br.com.alexalves.investimentosbrq.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity
class Usuario(
    var saldo: BigDecimal = BigDecimal(1000.0),
    var usd: Int = 0,
    var eur: Int = 0,
    var gbp: Int = 0,
    var ars: Int = 0,
    var cad: Int = 0,
    var aud: Int = 0,
    var jpy: Int = 0,
    var cny: Int = 0,
    var btc: Int = 0,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
){

}
