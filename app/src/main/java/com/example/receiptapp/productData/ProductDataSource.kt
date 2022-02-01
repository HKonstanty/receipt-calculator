package com.example.receiptapp.productData

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.receiptapp.receiptData.ReceiptDataSource
import com.example.receiptapp.receiptData.receiptsList

class ProductDataSource(resources: Resources) {
    private val initialProductList = productsList(resources)
    private val productLiveData = MutableLiveData(initialProductList)

    init {
        val currentList = productLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList.removeAll(updatedList)
            productLiveData.postValue(updatedList)
        }
    }

    fun addProduct(product: Product) {
        val currentList = productLiveData.value
        if (currentList == null) {
            productLiveData.postValue(listOf(product))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(0, product)
            productLiveData.postValue(updatedList)
        }
    }

    fun removeProduct(product: Product) {
        val currentList = productLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList.remove(product)
            productLiveData.postValue(updatedList)
        }
    }

    fun getProductForId(id: Long): Product? {
        productLiveData.value?.let { product ->
            return product.firstOrNull { it.id == id }
        }
        return null
    }

    fun getProductListForReceiptId(id: Long): List<Product>? {
        return productLiveData.value?.filter { it.receiptId == id }
    }

    fun getProductList(): LiveData<List<Product>> {
        return productLiveData
    }

    fun removeProductForReceiptId(id: Long) {
        val currentList = productLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList.removeIf { product -> product.receiptId == id }
            productLiveData.postValue(updatedList)
        }
    }

    companion object {
        private var INSTANCE: ProductDataSource? = null

        fun getDataSource(resources: Resources): ProductDataSource {
            return synchronized(ProductDataSource::class) {
                val newInstance = INSTANCE ?: ProductDataSource(resources)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}