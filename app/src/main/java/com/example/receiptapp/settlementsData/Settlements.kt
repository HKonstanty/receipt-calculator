package com.example.receiptapp.settlementsData

import android.content.res.Resources

fun settlementsList(resources: Resources): List<Settlement> {
    return listOf(
        Settlement(
            id = 1,
            senderId = 1,
            receiverId = 2,
            senderName = "Adam",
            receiverName = "Ewa",
            prize = 0.0,
            receipts = null
        ),
        /*Settlement(
            id = 2,
            senderId = 1,
            receiverId = 3,
            senderName = "Adam",
            receiverName = "Artur",
            prize = 10.44,
            receipts = null
        )*/
    )
}