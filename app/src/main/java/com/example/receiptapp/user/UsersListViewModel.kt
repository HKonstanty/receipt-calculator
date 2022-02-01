package com.example.receiptapp.user

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.receiptapp.userData.User
import com.example.receiptapp.userData.UserDataSource
import kotlin.random.Random

class UsersListViewModel(private val dataSource: UserDataSource): ViewModel() {
    val usersLiveData = dataSource.getUserList()

    fun insertUser(userName: String?, userNumber: String?, userEmail: String?) {
        if (userName == null || userNumber == null || userEmail == null) {
            return
        }

        val image = dataSource.getRandomFlowerImageAsset()
        val newUser = User(
            id = Random.nextLong(),
            name = userName,
            image = image,
            number = userNumber,
            email = userEmail
        )

        dataSource.addUser(newUser)
    }

    fun getIdForName(name: String): Long? {
        return dataSource.getIdForName(name)
    }

    fun getNameForId(id: Long): String {
        return dataSource.getUserForId(id)?.name.toString()
    }
}

class UsersListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsersListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UsersListViewModel(
                dataSource = UserDataSource.getDataSource(context.resources)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}