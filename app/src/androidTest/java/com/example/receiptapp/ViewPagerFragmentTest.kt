package com.example.receiptapp

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class ViewPagerFragmentTest {

    @get:Rule
    val activityScenario = activityScenarioRule<MainActivity>()

    @Test
    fun fragmentTest() {
        // VERIFY
        onView(withId(R.id.fragment_view_pager))
            .check(matches(isDisplayed()))
        onView(withId(R.id.fragment_receipt))
            .check(matches(isDisplayed()))
        onView(withId(R.id.fragment_receipt)).perform(swipeLeft())
        onView(withId(R.id.fragment_user))
            .check(matches(isDisplayed()))
        onView(withId(R.id.userRV))
           .check(matches(isDisplayed()))
        onView(withId(R.id.fragment_user)).perform(swipeLeft())
        onView(withId(R.id.fragment_settlements))
            .check(matches(isDisplayed()))
        //onView(withId(R.id.fragment_user)).perform(swipeLeft())
        //onView(withId(R.id.userRV)).perform(swipeLeft())



    }
}