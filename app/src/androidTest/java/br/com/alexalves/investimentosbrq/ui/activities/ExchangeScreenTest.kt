package br.com.alexalves.investimentosbrq.ui.activities

import android.content.Intent
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.alexalves.investimentosbrq.R
import br.com.alexalves.models.consts.ArgumentConsts
import br.com.alexalves.feature_exchange.repository.ExchangeRepository
import br.com.alexalves.feature_exchange.ui.activities.ExchangeActivity
import br.com.alexalves.feature_exchange.ui.viewmodels.ExchangeViewModel
import br.com.alexalves.models.Currency
import br.com.alexalves.models.User
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.core.IsInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import java.math.BigDecimal
import java.math.BigInteger

@RunWith(AndroidJUnit4::class)
class ExchangeScreenTest {

    @MockK
    lateinit var homeRepository: ExchangeRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        coEvery { homeRepository.searchUser(1L) } returns User(
            id = 1L,
            balance = BigDecimal(250),
            usd = BigInteger("20")
        )

        val exchangeModuleTest = module {
            viewModel<ExchangeViewModel>(override = true) { ExchangeViewModel(homeRepository) }
        }

        loadKoinModules(exchangeModuleTest)

        val currency =
            Currency("Dollar", BigDecimal(5), BigDecimal(4), 2.44, "USD")
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), ExchangeActivity::class.java)
        intent.putExtra(ArgumentConsts.currency_argument, currency)

        Intents.init()
        launchActivity<ExchangeActivity>(intent)
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testToolbar_inExchangeFragment() {

        onView(
            allOf(
                withParent(
                    allOf(
                        withId(R.id.toolbar_activity_cambio),
                        withParent(IsInstanceOf.instanceOf(ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        ).check(matches(isDisplayed()))

        onView(
            allOf(
                instanceOf(TextView::class.java),
                withText("Câmbio"),
                withParent(
                    allOf(
                        instanceOf(ConstraintLayout::class.java),
                        withParent(withId(R.id.toolbar_activity_cambio))
                    )
                )
            )
        ).check(matches(isDisplayed()))

        //Verify toolbar is displayed
        onView(
            allOf(
                instanceOf(Toolbar::class.java),
                withId(R.id.toolbar_activity_cambio)
            )
        ).check(matches(isDisplayed()))

        //Verify toolbar back option
        onView(
            allOf(
                withId(R.id.toolbar_back_option), withText("Moedas"),
                withParent(withParent(withId(R.id.toolbar_activity_cambio))),
                isDisplayed()
            )
        ).check(matches(withText("Moedas")))
    }

    @Test
    fun testCardCurrency_inExchangeFragment() {

        //Verify if cardView is displayed
        onView(
            allOf(
                instanceOf(CardView::class.java),
                withId(R.id.fragment_cambio_card_moeda),
                withChild(
                    allOf(
                        instanceOf(ConstraintLayout::class.java),
                    )
                )
            )
        ).check(matches(isDisplayed()))

        //Verify text in title of card
        onView(
            allOf(
                withParent(withParent(withId(R.id.fragment_cambio_card_moeda))),
                withId(R.id.fragment_cambio_text_titulo_moeda),
                instanceOf(TextView::class.java)
            )
        ).check(matches(withText("USD - Dollar")))

        //Verify text in variation of card
        onView(
            allOf(
                withParent(withParent(withId(R.id.fragment_cambio_card_moeda))),
                withId(R.id.fragment_cambio_text_variacao_moeda),
                instanceOf(TextView::class.java)
            )
        ).check(matches(withText("2,44%")))

        //Verify text in buy value of card
        onView(
            allOf(
                withParent(withParent(withId(R.id.fragment_cambio_card_moeda))),
                withId(R.id.fragment_cambio_text_valor_compra_moeda),
                instanceOf(TextView::class.java)
            )
        ).check(matches(withText("Compra: R$ 5,000")))

        //Verify text in sell value of card
        onView(
            allOf(
                withParent(withParent(withId(R.id.fragment_cambio_card_moeda))),
                withId(R.id.fragment_cambio_text_valor_venda_moeda),
                instanceOf(TextView::class.java)
            )
        ).check(matches(withText("Venda: R$ 4,000")))
    }

    @Test
    fun testBalanceAndAmountCurrency_inExchangeFragment() {
        onView(
            allOf(
                instanceOf(TextView::class.java),
                withId(R.id.fragment_cambio_text_saldo_disponivel)
            )
        ).check(matches(withText("Saldo disponível: R$ 250,00")))

        onView(
            allOf(
                instanceOf(TextView::class.java),
                withId(R.id.fragment_cambio_text_moeda_em_caixa)
            )
        ).check(matches(withText("20 Dollar em caixa")))
    }
}