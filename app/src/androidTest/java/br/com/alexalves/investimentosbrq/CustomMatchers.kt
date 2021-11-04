package br.com.alexalves.investimentosbrq

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

class CustomMatchers {

    companion object {

        fun verifyItemInPositionOfRecyclerView(
            position: Int,
            currencyAbbreviation: String,
            variation: String
        ): Matcher<View> {
            return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {

                override fun describeTo(description: Description?) {
                    description?.appendText("The abbreviation was not found in the list in position ${position} with abbreviation ${currencyAbbreviation} and variation ${variation}")
                }

                override fun matchesSafely(item: RecyclerView?): Boolean {
                    var validAbbreviation = false
                    var validVariation = false

                    val view = item?.findViewHolderForAdapterPosition(position)?.itemView
                    if (view != null) {
                        item.scrollToPosition(position)
                        val textAbbreviation = view.findViewById<TextView>(R.id.item_moeda_text_moeda)
                        validAbbreviation = textAbbreviation.text.toString() == currencyAbbreviation
                        val textVariation = view.findViewById<TextView>(R.id.item_moeda_text_variacao)
                        validVariation = textVariation.text.toString()==variation.toString()

                    }
                    return validAbbreviation && validVariation && isDisplayed().matches(view)
                }
            }
        }

        fun childAtPosition(
            parentMatcher: Matcher<View>,
            position: Int
        ): Matcher<View> {

            return object : TypeSafeMatcher<View>() {
                override fun describeTo(description: Description) {
                    description.appendText("Child at position $position in parent ")
                    parentMatcher.describeTo(description)
                }

                public override fun matchesSafely(view: View): Boolean {
                    val parent = view.parent
                    return parent is ViewGroup && parentMatcher.matches(parent)
                            && view == parent.getChildAt(position)
                }
            }
        }

    }
}