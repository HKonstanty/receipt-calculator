package com.example.receiptapp.settlementsData

data class Settlement (
    val id: Long,
    val senderId: Long,
    val receiverId: Long,
    val senderName: String,
    val receiverName: String,
    val prize: Double,
    val receipts: List<Long>?
    )