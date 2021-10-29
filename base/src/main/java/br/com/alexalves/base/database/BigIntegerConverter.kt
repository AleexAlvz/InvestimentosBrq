package br.com.alexalves.base.database

import androidx.room.TypeConverter
import java.math.BigInteger

class BigIntegerConverter {

    @TypeConverter
    fun BigIntegerToString(valor: BigInteger): String{
        return valor.toString()
    }

    @TypeConverter
    fun StringToBigInteger(valor: String): BigInteger{
        return valor.toBigInteger()
    }

}