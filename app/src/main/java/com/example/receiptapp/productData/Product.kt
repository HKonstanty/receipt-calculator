package com.example.receiptapp.productData

data class Product (
    val id: Long,
    val receiptId: Long,
    val name: String,
    var amount: Int,
    val prize: Double,
    val debtors: List<Long>?
)