package com.example.receiptapp.user

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.receiptapp.R
import com.google.android.material.textfield.TextInputEditText
import java.security.Permission
import java.util.jar.Manifest

const val USER_NAME = "name"
const val USER_NUMBER = "number"
const val USER_EMAIL = "email"
const val REQUEST_IMAGE_GALLERY = 123
const val REQUEST_IMAGE_CAMERA = 132

class AddUserActivity : AppCompatActivity() {
    private lateinit var addUserName: TextInputEditText
    private lateinit var addUserNumber: TextInputEditText
    private lateinit var addUserEmail: TextInputEditText
    private lateinit var addUserImage: ImageView
    private val usersListViewModel by viewModels<UsersListViewModel> {
        UsersListViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        findViewById<Button>(R.id.done_button).setOnClickListener {
            addUser()
        }
        addUserImage = findViewById(R.id.add_user_image)
        addUserImage.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Select image")
            builder.setMessage("Choose your option?")
            builder.setPositiveButton("Gallery") { dialog, which ->
                dialog.dismiss()

                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
            }
            builder.setNegativeButton("Camera") { dialog, which ->
                dialog.dismiss()
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAMERA)
                /*Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                    takePictureIntent.resolveActivity(packageManager)?.also {
                        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                        if (permission != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),1 )
                        } else {
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAMERA)
                        }
                    }
                }*/
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        addUserName = findViewById(R.id.add_user_name)
        addUserNumber = findViewById(R.id.add_user_number)
        addUserEmail = findViewById(R.id.add_user_email)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            addUserImage.setImageURI(data.data)
        } else if (requestCode == REQUEST_IMAGE_CAMERA && resultCode == Activity.RESULT_OK && data != null) {
            addUserImage.setImageBitmap(data.extras?.get("data") as Bitmap)
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addUser() {
        val resultIntent = Intent()

        if (addUserName.text.isNullOrEmpty() || addUserNumber.text.isNullOrEmpty() || addUserEmail.text.isNullOrEmpty()) {
            Toast.makeText(this, "Complete all info", Toast.LENGTH_SHORT).show()
            setResult(Activity.RESULT_CANCELED, resultIntent)
        } else if (usersListViewModel.getIdForName(addUserName.text.toString()) != null) {
            Toast.makeText(this, "User name is busy", Toast.LENGTH_SHORT).show()
        } else {
            val name = addUserName.text.toString()
            val number = addUserNumber.text.toString()
            val email = addUserEmail.text.toString()

            resultIntent.putExtra(USER_NAME, name)
            resultIntent.putExtra(USER_NUMBER, number)
            resultIntent.putExtra(USER_EMAIL, email)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

    }
}