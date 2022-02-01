package com.example.receiptapp.receiptData

import android.content.res.Resources
import com.example.receiptapp.R
import com.example.receiptapp.productData.Product

fun receiptsList(resources: Resources): List<Receipt> {
    return listOf(
        Receipt(
            id = 1,
            name = "Wycieczka",
            image = R.drawable.ic_baseline_article_24,
            owner = 1,
            prize = 12.40,
            debtors = listOf(1, 2),
            products = listOf(
                Product(
                    id = 1,
                    receiptId = 1,
                    name = "Zupa",
                    prize = 5.05,
                    amount = 2,
                    debtors = listOf(1, 2, 3)
                ),
                Product(
                    id = 2,
                    receiptId = 1,
                    name = "Picie",
                    prize = 2.30,
                    amount = 1,
                    debtors = listOf(1, 2)
                )
            )
        ),
        /*Receipt(
            id = 2,
            name = "Obiad",
            image = R.drawable.ic_baseline_article_24,
            owner = 1,
            prize = 25.00,
            debtors = listOf(2, 3, 4),
            products = listOf(
                Product(
                    id = 1,
                    receiptId = 2,
                    name = "Pizza",
                    prize = 10.00,
                    amount = 2,
                    debtors = listOf(1, 2, 3, 4)
                ),
                Product(
                    id = 2,
                    receiptId = 2,
                    name = "Picie",
                    prize = 5.00,
                    amount = 1,
                    debtors = listOf(1, 2, 3, 4)
                )
            )
        )*/
    )
}