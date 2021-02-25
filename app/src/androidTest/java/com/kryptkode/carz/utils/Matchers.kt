package com.kryptkode.carz.utils

import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher

fun hasTextInputLayoutErrorText(expectedErrorText: String): Matcher<View?> {
    return object : TypeSafeMatcher<View?>() {

        override fun matchesSafely(item: View?): Boolean {
            if (item !is TextInputLayout) {
                return false
            }

            val error = item.error ?: return false

            val hint = error.toString()

            return expectedErrorText == hint
        }

        override fun describeTo(description: Description?) {
        }
    }
}

fun withToolbarTitle(expectedTitle: CharSequence): Matcher<View> {
    return object : BoundedMatcher<View, Toolbar>(Toolbar::class.java) {
        override fun describeTo(description: Description?) {
            description?.appendText("with toolbar title: $expectedTitle")
        }

        override fun matchesSafely(toolbar: Toolbar?): Boolean {
            return expectedTitle == toolbar?.title
        }
    }
}

fun withRecyclerView(@IdRes resId: Int): Matcher<View> {
    return allOf(isDisplayed(), withId(resId))
}
