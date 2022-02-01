package com.example.receiptapp.receiptData

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ReceiptDataSource(resources: Resources) {
    private val initialReceiptList = receiptsList(resources)
    //private val initialReceiptList = arrayListOf<Receipt>()
    private val receiptsLiveData = MutableLiveData(initialReceiptList)

    init {
        val currentList = receiptsLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList.removeAll(updatedList)
            receiptsLiveData.postValue(updatedList)
        }
    }

    fun addReceipt(receipt: Receipt) {
        val currentList = receiptsLiveData.value
        if (currentList == null) {
            receiptsLiveData.postValue(listOf(receipt))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(0, receipt)
            receiptsLiveData.postValue(updatedList)
        }
    }

    fun removeReceipt(receipt: Receipt) {
        val currentList = receiptsLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList.remove(receipt)
            receiptsLiveData.postValue(updatedList)
        }
    }

    fun getReceiptForId(id: Long): Receipt? {
        receiptsLiveData.value?.let { receipts ->
            return receipts.firstOrNull { it.id == id }
        }
        return null
    }

    fun getReceiptList(): LiveData<List<Receipt>> {
        return receiptsLiveData
    }

    fun getRandomReceiptImageAsset(): Int? {
        val randomNumber = (initialReceiptList.indices).random()
        return initialReceiptList[randomNumber].image
    }

    companion object {
        private var INSTANCE: ReceiptDataSource? = null

        fun getDataSource(resources: Resources): ReceiptDataSource {
            return synchronized(ReceiptDataSource::class) {
                val newInstance = INSTANCE ?: ReceiptDataSource(resources)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}