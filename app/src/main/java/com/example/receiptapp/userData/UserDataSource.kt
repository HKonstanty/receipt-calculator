package com.example.receiptapp.userData

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/* Handles operations on usersLiveData and holds details about it. */
class UserDataSource(resources: Resources) {
    private val initialUserList = usersList(resources)
    private val usersLiveData = MutableLiveData(initialUserList)

    /* Adds users to liveData and posts value. */
    fun addUser(user: User) {
        val currentList = usersLiveData.value
        if (currentList == null) {
            usersLiveData.postValue(listOf(user))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(0, user)
            usersLiveData.postValue(updatedList)
        }
    }

    /* Removes users from liveData and posts value. */
    fun removeUser(user: User) {
        val currentList = usersLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList.remove(user)
            usersLiveData.postValue(updatedList)
        }
    }

    /* Returns user given an ID. */
    fun getUserForId(id: Long): User? {
        usersLiveData.value?.let { users ->
            return users.firstOrNull{ it.id == id}
        }
        return null
    }

    fun getIdForName(name: String): Long? {
        usersLiveData.value?.let { users ->
            return users.firstOrNull{ it.name == name}?.id
        }
        return null
    }

    fun getUserList(): LiveData<List<User>> {
        return usersLiveData
    }

    /* Returns a random user asset for users that are added. */
    fun getRandomFlowerImageAsset(): Int? {
        val randomNumber = (initialUserList.indices).random()
        return initialUserList[randomNumber].image
    }

    companion object {
        private var INSTANCE: UserDataSource? = null

        fun getDataSource(resources: Resources): UserDataSource {
            return synchronized(UserDataSource::class) {
                val newInstance = INSTANCE ?: UserDataSource(resources)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}