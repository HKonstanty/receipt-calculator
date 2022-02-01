package com.example.receiptapp.productData

import android.content.res.Resources

fun productsList(resources: Resources): List<Product> {
    return listOf(
        Product(
            id = 1,
            receiptId = 1,
            name = "Zupa",
            prize = 5.05,
            amount = 2,
            debtors = listOf(1, 2, 3)
        ),
        /*Product(
            id = 2,
            receiptId = 1,
            name = "Picie",
            prize = 2.30,
            amount = 1,
            debtors = listOf(1, 2)
        ),
        Product(
            id = 3,
            receiptId = 2,
            name = "Pizza",
            prize = 10.00,
            amount = 2,
            debtors = listOf(1, 2, 3, 4)
        ),
        Product(
            id = 4,
            receiptId = 2,
            name = "Picie",
            prize = 5.00,
            amount = 1,
            debtors = listOf(1, 2, 3, 4)
        )*/
    )
}