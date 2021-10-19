package br.com.alexalves.investimentosbrq.ui.activities

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import br.com.alexalves.investimentosbrq.R
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val activity = ActivityScenarioRule(HomeActivity::class.java)

    @Test
    fun when_StartHomeActivity_then_RecyclerViewCurrencies_isDisplayed(){
        onView(withId(R.id.recycler_view_moedas_home))
            .check(matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun when_StartHomeActivity_then_view3Currencies(){
        onView(withText("BTC")).check(matches(isDisplayed()))
        onView(withText("EUR")).check(matches(isDisplayed()))
        onView(withText("USD")).check(matches(isDisplayed()))
    }


}