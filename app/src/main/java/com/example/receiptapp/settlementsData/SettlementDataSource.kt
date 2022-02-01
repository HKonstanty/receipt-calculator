package com.example.receiptapp.settlementsData

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.receiptapp.userData.User
import com.example.receiptapp.userData.UserDataSource

class SettlementDataSource(resources: Resources) {
    private val initialSettlementList = settlementsList(resources)
    private val settlementsLiveData = MutableLiveData(initialSettlementList)

    init {
        val currentList = settlementsLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList.removeAll(updatedList)
            settlementsLiveData.postValue(updatedList)
        }
    }

    fun addSettlement(settlement: Settlement) {
        val currentList = settlementsLiveData.value
        if (currentList == null) {
            settlementsLiveData.postValue(listOf(settlement))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(0, settlement)
            settlementsLiveData.postValue(updatedList)
        }
    }

    fun addSettlementList(settlementsList: List<Settlement>) {
        val updatedList = settlementsList.toMutableList()
        settlementsLiveData.postValue(updatedList)
    }

    fun removeSettlement(settlement: Settlement) {
        val currentList = settlementsLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList.remove(settlement)
            settlementsLiveData.postValue(updatedList)
        }
    }

    fun getSettlementForId(id: Long): Settlement? {
        settlementsLiveData.value?.let { settlement ->
            return settlement.firstOrNull{ it.id == id}
        }
        return null
    }

    fun getSettlementList(): LiveData<List<Settlement>> {
        return settlementsLiveData
    }

    fun clear() {
        val currentList = settlementsLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList.removeAll(updatedList)
            settlementsLiveData.postValue(updatedList)
        }
    }

    companion object {
        private var INSTANCE: SettlementDataSource? = null

        fun getDataSource(resources: Resources): SettlementDataSource {
            return synchronized(SettlementDataSource::class) {
                val newInstance = INSTANCE ?: SettlementDataSource(resources)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}