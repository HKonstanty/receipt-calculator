package com.example.receiptapp

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @get:Rule
    val activityScenario = activityScenarioRule<MainActivity>()

    @Test
    fun MainActivityTest() {
        // VERIFY
        /*Espresso.onView(ViewMatchers.withId(R.id.splash_fragment))
            .check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))*/
        onView(withId(R.id.fragment_view_pager))
            .check(matches(isDisplayed()))
        onView(withId(R.id.add_receipt_floating_action_button))
            .check(matches(isDisplayed()))
        onView(withId(R.id.fragment_receipt))
            .check(matches(isDisplayed()))
        onView(withId(R.id.add_receipt_floating_action_button))
            .perform(click())
        onView(withId(R.id.constraintLayout))
            .check(matches(isDisplayed()))
        Espresso.pressBack()
        onView(withId(R.id.add_receipt_floating_action_button))
            .check(matches(isDisplayed()))

    }
}