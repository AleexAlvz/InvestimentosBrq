package br.com.alexalves.investimentosbrq.ui.activities

import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.alexalves.investimentosbrq.CustomMatchers.Companion.childAtPosition
import br.com.alexalves.investimentosbrq.CustomMatchers.Companion.verifyItemInPositionOfRecyclerView
import br.com.alexalves.investimentosbrq.R
import br.com.alexalves.investimentosbrq.model.Currency
import br.com.alexalves.investimentosbrq.model.User
import br.com.alexalves.investimentosbrq.repository.ExchangeRepository
import br.com.alexalves.investimentosbrq.ui.adapter.CurrencyAdapter
import br.com.alexalves.investimentosbrq.viewmodel.HomeViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import java.math.BigDecimal

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @MockK
    lateinit var homeRepository: ExchangeRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        val currencies = listOf(
            Currency("Bitcoin", BigDecimal.ZERO, BigDecimal.ZERO, 0.09, "BTC"),
            Currency("Euro", BigDecimal.ZERO, BigDecimal.ZERO, 0.10, "EUR"),
            Currency("Dollar", BigDecimal.ZERO, BigDecimal.ZERO, 0.11, "USD"),
        )

        coEvery { homeRepository.searchCurrencies() } returns currencies
        coEvery { homeRepository.searchUser(1L) } returns User(id = 1L)

        val homeTestModule = module {
            viewModel<HomeViewModel>(override = true) { HomeViewModel(homeRepository) }
        }

        loadKoinModules(homeTestModule)

        Intents.init()
        launchActivity<HomeActivity>()

    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testToolbar() {
        //Verify title Moedas in toolbar
        onView(
            allOf(
                instanceOf(TextView::class.java),
                withText("Moedas"),
                withParent(
                    allOf(
                        instanceOf(ConstraintLayout::class.java),
                        withParent(withId(R.id.toolbar_activity_home))
                    )
                )
            )
        ).check(matches(isDisplayed()))

        //Verify toolbar is displayed
        onView(
            allOf(
                instanceOf(Toolbar::class.java),
                withId(R.id.toolbar_activity_home)
            )
        ).check(matches(isDisplayed()))
    }

    @Test
    fun testRecyclerView() {
        onView(withId(R.id.recycler_view_moedas_home))
            .perform(RecyclerViewActions.scrollToPosition<CurrencyAdapter.CurrenciesViewHolder>(0))
            .check(matches(verifyItemInPositionOfRecyclerView(0, "BTC", "0,09%")))
            .perform(RecyclerViewActions.scrollToPosition<CurrencyAdapter.CurrenciesViewHolder>(1))
            .check(matches(verifyItemInPositionOfRecyclerView(1, "EUR", "0,10%")))
            .perform(RecyclerViewActions.scrollToPosition<CurrencyAdapter.CurrenciesViewHolder>(2))
            .check(matches(verifyItemInPositionOfRecyclerView(2, "USD", "0,11%")))
    }

    @Test
    fun testClickInRecyclerViewList() {
        //Test click in first item of List Currencies
        val recyclerView = onView(
            allOf(
                withId(R.id.recycler_view_moedas_home),
                childAtPosition(
                    withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    1
                )
            )
        )

        recyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        //Verify if is called ExchangeFragment
        intended(hasComponent(ExchangeActivity::class.qualifiedName))
    }
}

