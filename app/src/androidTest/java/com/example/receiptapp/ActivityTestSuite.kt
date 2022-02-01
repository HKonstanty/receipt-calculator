package com.example.receiptapp

import com.example.receiptapp.receipt.AddReceiptActivityTest
import com.example.receiptapp.user.AddUserActivityTest
import com.example.receiptapp.user.UserFragmentTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(MainActivityTest::class, ViewPagerFragmentTest::class, AddUserActivityTest::class,
    UserFragmentTest::class, AddReceiptActivityTest::class)
class ActivityTestSuite