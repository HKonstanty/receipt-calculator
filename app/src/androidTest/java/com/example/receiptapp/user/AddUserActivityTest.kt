package com.example.receiptapp.user

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.receiptapp.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class AddUserActivityTest {

    @get:Rule
    val activityScenario = activityScenarioRule<AddUserActivity>()

    @Test
    fun check_input_fields() {
        onView(withId(R.id.textView))
            .check(matches(withText(R.string.add_user)))
        onView(withId(R.id.done_button))
            .check(matches(isDisplayed()))
        onView(withId(R.id.add_user_email))
            .perform(typeText("abc@onet.pl"))
        onView(withId(R.id.add_user_number))
            .perform(typeText("1233123"))
        onView(withId(R.id.add_user_name))
            .perform(typeText("Ala"))
        Espresso.pressBack()
        onView(withId(R.id.done_button))
            .check(matches(isDisplayed()))
    }
}