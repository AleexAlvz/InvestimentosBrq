package br.com.alexalves.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.math.BigInteger

@Entity
data class User(
    @SerializedName("saldo")
    var balance: BigDecimal = BigDecimal(10000000.0),
    var usd: BigInteger = BigInteger.ZERO,
    var eur: BigInteger = BigInteger.ZERO,
    var gbp: BigInteger = BigInteger.ZERO,
    var ars: BigInteger = BigInteger.ZERO,
    var cad: BigInteger = BigInteger.ZERO,
    var aud: BigInteger = BigInteger.ZERO,
    var jpy: BigInteger = BigInteger.ZERO,
    var cny: BigInteger = BigInteger.ZERO,
    var btc: BigInteger = BigInteger.ZERO,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
)
