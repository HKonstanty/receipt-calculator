package com.example.receiptapp.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.receiptapp.userData.User
import androidx.recyclerview.widget.ListAdapter
import com.example.receiptapp.R

class UsersAdapter(private val onClick: (User) -> Unit) :
    androidx.recyclerview.widget.ListAdapter<User, UsersAdapter.UserViewHolder>(UserDiffCallback) {

    class UserViewHolder(itemView: View, val onClick: (User) -> Unit): RecyclerView.ViewHolder(itemView) {
        private val userTextView: TextView = itemView.findViewById(R.id.nickTV)
        private val userImageView: ImageView = itemView.findViewById(R.id.avatarIV)
        private var currentUser: User? = null

        init {
            itemView.setOnClickListener {
                currentUser?.let {
                    onClick(it)
                }
            }
        }

        /* Bind user name and image. */
        fun bind(user: User) {
            currentUser = user

            userTextView.text = user.name
            if (user.image != null) {
                userImageView.setImageResource(user.image)
            } else {
                userImageView.setImageResource(R.drawable.ic_avatar)
            }
        }
    }

    /* Creates and inflates view and return UserViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view, onClick)
    }

    /* Gets current user and uses it to bind view. */
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }
}

object UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }
}