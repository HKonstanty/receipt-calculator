package com.example.receiptapp.user

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.example.receiptapp.R
import com.example.receiptapp.userData.User
import com.example.receiptapp.userDetail.UserDetailActivity1
import com.google.android.material.floatingactionbutton.FloatingActionButton

const val USER_ID = "user id"

class UserFragment : Fragment() {

    private var names = mutableListOf<String>()
    private var images = mutableListOf<Int>()

    private val newUserActivityRequestCode = 1
    private val usersListViewModel by viewModels<UsersListViewModel> {
        UsersListViewModelFactory(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        val rv = view.findViewById<RecyclerView>(R.id.userRV)
        //postToList()
        //rv.adapter = UserRecyclerAdapter(names, images)
        val userAdapter = UsersAdapter { user -> adapterOnClick(user)}
        rv.adapter = userAdapter

        usersListViewModel.usersLiveData.observe(viewLifecycleOwner) {
            it?.let {
                userAdapter.submitList(it as MutableList<User>)
            }
        }

        val bt = view.findViewById<FloatingActionButton>(R.id.addUserFBT)
        bt.setOnClickListener {
            Toast.makeText(activity, "Add user clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, AddUserActivity::class.java)
            startActivityForResult(intent, newUserActivityRequestCode)
        }
        return view
    }

    private fun adapterOnClick(user: User) {
        val intent = Intent(context, UserDetailActivity1::class.java)
        intent.putExtra(USER_ID, user.id)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        /* Inserts user into viewModel. */
        if (requestCode == newUserActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.let { data ->
                val userName = data.getStringExtra(USER_NAME)
                val userNumber = data.getStringExtra(USER_NUMBER)
                val userEmail = data.getStringExtra(USER_EMAIL)


                usersListViewModel.insertUser(userName, userNumber, userEmail)
            }
        }
    }

    /*private fun addToList(title: String, image: Int) {
        names.add(title)
        images.add(image)
    }

    private fun postToList() {
        for (i in 1..5) {
            addToList("title $i", R.mipmap.ic_launcher_round)
        }
    }*/
}