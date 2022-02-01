package com.example.receiptapp.product

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.receiptapp.productData.Product
import com.example.receiptapp.productData.ProductDataSource
import kotlin.random.Random

class ProductsListViewModel(private val dataSource: ProductDataSource): ViewModel() {
    val productsLiveData = dataSource.getProductList()

    fun insertProduct(productName: String, productReceiptId: Long, productAmount: Int, productPrize: Double,
        productDebtors: List<Long>) {
        if (productName == null || productReceiptId == null || productAmount == null || productPrize == null ||
                productDebtors == null) {
            return
        }

        val newProduct = Product(
            id = Random.nextLong(),
            receiptId = productReceiptId,
            name = productName,
            amount = productAmount,
            prize = productPrize,
            debtors = productDebtors
        )

        dataSource.addProduct(newProduct)
    }

    fun removeProduct(product: Product) {
        dataSource.removeProduct(product)
    }

    fun removeProductId(id: Long) {
        val product = dataSource.getProductForId(id)
        product?.let { dataSource.removeProduct(it) }
    }

    fun getProductListForReceiptId(id: Long): List<Product>? {
        return dataSource.getProductListForReceiptId(id)
    }

    fun removeProductForReceiptId(id: Long) {
        dataSource.removeProductForReceiptId(id)
    }
}

class ProductListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductsListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductsListViewModel(
                dataSource = ProductDataSource.getDataSource(context.resources)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}