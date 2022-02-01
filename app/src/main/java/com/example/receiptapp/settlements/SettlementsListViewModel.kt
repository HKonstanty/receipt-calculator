package com.example.receiptapp.settlements

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.receiptapp.settlementsData.Settlement
import com.example.receiptapp.settlementsData.SettlementDataSource
import kotlin.random.Random

class SettlementsListViewModel(private val dataSource: SettlementDataSource): ViewModel() {
    val settlementsLiveData = dataSource.getSettlementList()

    fun insertSettlement(settlementSenderId:Long, settlementReceiverId: Long, settlementSender: String, settlementReceiver: String, settlementPrize: Double) {
        val newSettlement = Settlement(
            id = Random.nextLong(),
            senderId = settlementSenderId,
            senderName =  settlementSender,
            receiverId = settlementReceiverId,
            receiverName = settlementReceiver,
            prize = settlementPrize,
            receipts = null
        )

        dataSource.addSettlement(newSettlement)
    }

    fun updateSettlements(settlementsList: List<Settlement>) {
        dataSource.addSettlementList(settlementsList)
    }

    fun clearSettlements() {
        dataSource.clear()
    }
}

class SettlementsListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettlementsListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettlementsListViewModel(
                dataSource = SettlementDataSource.getDataSource(context.resources)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}