package br.com.alexalves.investimentosbrq.database

import androidx.room.TypeConverter
import java.math.BigDecimal

class BigDecimalConverter {

    @TypeConverter
    fun BigDecimalToDouble(valor: BigDecimal): Double{
        return valor.toDouble()
    }

    @TypeConverter
    fun DoubleToBigDecimal(valor: Double): BigDecimal{
        return valor.toBigDecimal()
    }

}
