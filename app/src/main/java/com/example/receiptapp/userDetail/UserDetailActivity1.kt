package com.example.receiptapp.userDetail

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.receiptapp.R
import com.example.receiptapp.user.USER_ID

class UserDetailActivity1 : AppCompatActivity() {
    private val userDetailViewModel by viewModels<UserDetailViewModel> {
        UserDetailViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_detail_activity1)

        var currentUserId: Long? = null

        /* Connect variables to UI elements. */
        val userName: TextView = findViewById(R.id.user_detail_name)
        val userImage: ImageView = findViewById(R.id.flower_detail_image)
        val userEmail: TextView = findViewById(R.id.user_detail_email)
        val userNumber: TextView = findViewById(R.id.user_detail_number)
        val removeFlowerButton: Button = findViewById(R.id.remove_button)

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            currentUserId = bundle.getLong(USER_ID)
        }

        /* If currentUserId is not null, get corresponding user*/
        currentUserId?.let {
            val currentUser = userDetailViewModel.getUserForId(it)
            userName.text = currentUser?.name
            if (currentUser?.image == null) {
                userImage.setImageResource(R.drawable.ic_avatar)
            } else {
                userImage.setImageResource(currentUser.image)
            }
            userName.text = currentUser?.name
            userEmail.text = currentUser?.email
            userNumber.text = currentUser?.number

            removeFlowerButton.setOnClickListener {
                if (currentUser != null) {
                    userDetailViewModel.removeUser(currentUser)
                }
                finish()
            }
        }
    }
}