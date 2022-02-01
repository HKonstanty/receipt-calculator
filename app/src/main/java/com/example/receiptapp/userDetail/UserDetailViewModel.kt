package com.example.receiptapp.userDetail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.receiptapp.userData.User
import com.example.receiptapp.userData.UserDataSource

class UserDetailViewModel(private val dataSource: UserDataSource): ViewModel() {
    fun getUserForId(id: Long): User? {
        return dataSource.getUserForId(id)
    }

    fun removeUser(user: User) {
        dataSource.removeUser(user)
    }
}

class UserDetailViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserDetailViewModel(
                dataSource = UserDataSource.getDataSource(context.resources)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}