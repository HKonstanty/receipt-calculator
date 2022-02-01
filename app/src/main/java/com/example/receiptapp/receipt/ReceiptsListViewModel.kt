package com.example.receiptapp.receipt

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.receiptapp.productData.Product
import com.example.receiptapp.receiptData.Receipt
import com.example.receiptapp.receiptData.ReceiptDataSource
import kotlin.random.Random


class ReceiptsListViewModel(private val dataSource: ReceiptDataSource): ViewModel() {
    val receiptsLiveData = dataSource.getReceiptList()

    fun insertReceipt(receiptId: Long, receiptName: String?, receiptOwner: Long?, receiptPrize: Double?, receiptProducts: List<Product>?, receiptImage: Int?, receiptDebtors: List<Long>?) {
        if (receiptName == null || receiptOwner == null || receiptPrize == null) {
            return
        }

        val newReceipt = Receipt(
            id = receiptId,
            name = receiptName,
            image = receiptImage ?: dataSource.getRandomReceiptImageAsset(),
            owner = receiptOwner,
            prize = receiptPrize,
            products = receiptProducts,
            debtors = receiptDebtors
        )

        dataSource.addReceipt(newReceipt)
    }

    fun removeReceipt(receipt: Receipt) {
        dataSource.removeReceipt(receipt)
    }

    fun removeReceiptId(id: Long) {
        val receipt = dataSource.getReceiptForId(id)
        receipt?.let { dataSource.removeReceipt(it) }
    }

    fun getReceiptById(id: Long): Receipt? {
        return dataSource.getReceiptForId(id)
    }

    fun getReceiptList(): LiveData<List<Receipt>> {
        return receiptsLiveData
    }
}

class ReceiptsListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReceiptsListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReceiptsListViewModel(
                dataSource = ReceiptDataSource.getDataSource(context.resources)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}