package com.example.receiptapp.user

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.receiptapp.MainActivity
import com.example.receiptapp.R
import com.example.receiptapp.userData.usersList
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class UserFragmentTest {

    @get:Rule
    val activityScenario = activityScenarioRule<MainActivity>()
    private val LIST_ITEM_NUMBER = 0
    private val initialUser = usersList()[LIST_ITEM_NUMBER]

    @Test
    fun user_recycler_view_test() {
        onView(withId(R.id.fragment_view_pager))
            .check(matches(isDisplayed()))
        onView(withId(R.id.fragment_receipt)).perform(ViewActions.swipeLeft())
        onView(withId(R.id.fragment_user))
            .check(matches(isDisplayed()))
        onView(withId(R.id.userRV))
            .check(matches(isDisplayed()))
    }

    @Test
    fun select_user_test() {
        onView(withId(R.id.fragment_receipt)).perform(ViewActions.swipeLeft())
        onView(withId(R.id.userRV))
            .check(matches(isDisplayed()))
        onView(withId(R.id.userRV))
            .perform(actionOnItemAtPosition<UsersAdapter.UserViewHolder>(LIST_ITEM_NUMBER, click()))
        onView(withId(R.id.user_detail_activity))
            .check(matches(isDisplayed()))
        onView(withId(R.id.user_detail_name))
            .check(matches(withText(initialUser.name)))
        onView(withId(R.id.user_detail_number))
            .check(matches(withText(initialUser.number)))
        onView(withId(R.id.user_detail_email))
            .check(matches(withText(initialUser.email)))
    }
}