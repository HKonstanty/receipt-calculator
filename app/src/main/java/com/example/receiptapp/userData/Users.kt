package com.example.receiptapp.userData

import android.content.res.Resources
import com.example.receiptapp.R

/* Returns initial list of users. */
fun usersList(resources: Resources): List<User> {
    return listOf(
        User(
            id = 1,
            name = "Adam",
            image = R.drawable.ic_avatar,
            number = "123456789",
            email = "abc@123.pl"
        ),
        User(
            id = 2,
            name = "Ewa",
            image = R.drawable.ic_avatar,
            number = "123456789",
            email = "abc@123.pl"
        ),
        User(
            id = 3,
            name = "Artur",
            image = R.drawable.ic_avatar,
            number = "123456789",
            email = "abc@123.pl"
        ),
        User(
            id = 4,
            name = "Jacek",
            image = R.drawable.ic_avatar,
            number = "123456789",
            email = "abc@123.pl"
        )
    )
}

fun usersList(): List<User> {
    return listOf(
        User(
            id = 1,
            name = "Adam",
            image = R.drawable.ic_avatar,
            number = "123456789",
            email = "abc@123.pl"
        ),
        User(
            id = 2,
            name = "Ewa",
            image = R.drawable.ic_avatar,
            number = "123456789",
            email = "abc@123.pl"
        ),
        User(
            id = 3,
            name = "Artur",
            image = R.drawable.ic_avatar,
            number = "123456789",
            email = "abc@123.pl"
        ),
        User(
            id = 4,
            name = "Jacek",
            image = R.drawable.ic_avatar,
            number = "123456789",
            email = "abc@123.pl"
        )
    )
}