package br.com.alexalves.investimentosbrq.ui.activities

import android.content.Intent
import androidx.test.core.app.launchActivity
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.alexalves.investimentosbrq.consts.ArgumentConsts
import br.com.alexalves.investimentosbrq.model.Currency
import io.mockk.MockKAnnotations
import org.junit.Before
import org.junit.runner.RunWith
import java.math.BigDecimal

@RunWith(AndroidJUnit4::class)
class CambioScreenTest{

    @Before
    fun setup(){
        MockKAnnotations.init(this, relaxUnitFun = true)
        Intents.init()

        val currency = Currency("Dollar", BigDecimal(5), BigDecimal(4), 2.435, "USD")
        val intent = Intent()
        intent.putExtra(ArgumentConsts.currency_argument, currency)

        launchActivity<ExchangeActivity>()
    }

}