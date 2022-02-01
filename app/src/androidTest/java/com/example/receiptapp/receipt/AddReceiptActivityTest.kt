package com.example.receiptapp.receipt

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.receiptapp.R
import com.example.receiptapp.user.AddUserActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class AddReceiptActivityTest {

    @get:Rule
    val activityScenario = activityScenarioRule<AddReceiptActivity>()

    @Test
    fun add_receipt_input_test() {
        onView(withId(R.id.nameTF))
            .check(matches(isDisplayed()))
        onView(withId(R.id.prizeTF))
            .check(matches(isDisplayed()))
        onView(withId(R.id.receipt_owner))
            .check(matches(isDisplayed()))
        onView(withId(R.id.receipt_debtors_text_view))
            .check(matches(isDisplayed()))
        onView(withId(R.id.receipt_debtors_text_view))
            .perform(click())
        onView(withText("Select debtors"))
            .check(matches(isDisplayed()))
    }
}