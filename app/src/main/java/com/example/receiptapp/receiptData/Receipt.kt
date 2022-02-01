package com.example.receiptapp.receiptData

import androidx.annotation.DrawableRes
import com.example.receiptapp.productData.Product

data class Receipt(
    val id: Long,
    val name: String?,
    @DrawableRes
    val image: Int?,
    val owner: Long,
    val prize: Double,
    val debtors: List<Long>?,
    val products: List<Product>?
    )