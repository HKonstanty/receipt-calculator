package com.example.receiptapp.receipt

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.receiptapp.R
import com.example.receiptapp.product.AddProductActivity
import com.example.receiptapp.user.REQUEST_IMAGE_CAMERA
import com.example.receiptapp.user.REQUEST_IMAGE_GALLERY
import com.example.receiptapp.user.UsersListViewModel
import com.example.receiptapp.user.UsersListViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

const val RECEIPT_TITLE = "title"
const val RECEIPT_PRIZE = "prize"
const val RECEIPT_OWNER = "owner"
const val RECEIPT_DEBTORS = "debtors"
const val RECEIPT_FOTO = "foto"
const val CAMERA_REQUEST_CODE = 42
const val REQUEST_TAKE_FOTO = 24
const val PERMISSION_CODE = 0
const val CHOOSE_IMAGE_REQUEST = 123
private lateinit var photoFile: File
private const val FILE_NAME = "photo.jpg"

class AddReceiptActivity : AppCompatActivity() {

    private val newReceiptActivityRequestCode = 1
    private val usersListViewModel by viewModels<UsersListViewModel> {
        UsersListViewModelFactory(this)
    }
    private val receiptsListViewModel by viewModels<ReceiptsListViewModel> {
        ReceiptsListViewModelFactory(this)
    }

    private var selectedDebtors = mutableListOf<Boolean>()
    //private var debtorsList = mutableListOf<Int>()
    private lateinit var usersList: List<String>
    private lateinit var receiptDebtorsTextView: TextView

    private lateinit var addReceiptTile: TextInputEditText
    private lateinit var addReceiptPrize: TextInputEditText
    private lateinit var addReceiptOwner: AutoCompleteTextView
    private lateinit var addReceiptImageView: ImageView
    private var receiptId: Long = -1
    //private lateinit var mCurrentPhotoPath: String
    //private lateinit var photoPath: String
    private lateinit var fotoUriPath: Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_receipt_activity)
        receiptDebtorsTextView = findViewById(R.id.receipt_debtors_text_view)
        receiptDebtorsTextView.setOnClickListener {
            showDebtorsDialog()
        }

        usersList = getUsersName()
        val adapter = ArrayAdapter(this, R.layout.dropdown_list_item, usersList)
        findViewById<AutoCompleteTextView>(R.id.receipt_owner).setAdapter(adapter)

        findViewById<Button>(R.id.receipt_save).setOnClickListener {
            addReceipt()
        }

        receiptId = Random.nextLong()
        Log.i("receiptId in add receipt", receiptId.toString())
        findViewById<Button>(R.id.receipt_add_product).setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            intent.putExtra(RECEIPT_ID, receiptId)
            Toast.makeText(this, "add product", Toast.LENGTH_SHORT).show()
            //Log.i("uri", fotoUriPath.toString())
            //intent.putExtra(RECEIPT_FOTO, fotoUriPath)
            startActivityForResult(intent, 1)
        }

        findViewById<Button>(R.id.cameraBT).setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
        }

        findViewById<Button>(R.id.galleryBT).setOnClickListener {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                val permission = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permission, PERMISSION_CODE)
            } else {
                chooseFoto()
            }
        }

        addReceiptTile = findViewById(R.id.nameTF)
        addReceiptPrize = findViewById(R.id.prizeTF)
        addReceiptOwner = findViewById(R.id.receipt_owner)
        addReceiptImageView = findViewById(R.id.imageView5)
    }

    private fun chooseFoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
    }

    /*private fun takeFoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //photoFile = getPhotoFile(FILE_NAME)
        photoFile = createImageFile2()
        //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile)
        val fileProvider = FileProvider.getUriForFile(this, "com.example.receiptapp.receipt.fileprovider", photoFile)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)

    }*/

    /*private fun takeFoto2() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        var photoFile: File? = null
        try {
            photoFile = createImageFile()
        } catch (e: IOException) {}
        if (photoFile != null) {
            val photoUri = FileProvider.getUriForFile(
                this,
                "com.example.receiptapp.receipt.fileprovider",
                photoFile
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            startActivityForResult(intent, REQUEST_TAKE_FOTO)
        }
    }*/

    /*private fun createImageFile(): File? {

        val fileName = "MyPicture"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            fileName, ".jpg", storageDir
        )
        photoPath = image.absolutePath

        return image
    }*/

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseFoto()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

   /* @Throws(IOException::class)
    private fun createImageFile2(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }*/

    /*private fun getPhotoFile(fileName: String): File {
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }*/

    private fun addReceipt() {
        val resultIntent = Intent()
        setResult(Activity.RESULT_OK, resultIntent)

        if (addReceiptTile.text.isNullOrEmpty() || addReceiptPrize.text.isNullOrEmpty() || addReceiptOwner.text.isNullOrEmpty()) {
            Toast.makeText(this, "Complete all info", Toast.LENGTH_SHORT).show()
        } else {
            val title = addReceiptTile.text.toString()
            val prize = addReceiptPrize.text.toString().toDouble()
            val owner = addReceiptOwner.text.toString()
            val ownerId = usersListViewModel.getIdForName(owner)
            val debtors = mutableListOf<Long>()
            for (i in selectedDebtors.indices) {
                if (selectedDebtors[i]) {
                    usersListViewModel.getIdForName(usersList[i])?.let { debtors.add(it) }
                }
            }
            receiptsListViewModel.insertReceipt(receiptId, title, ownerId, prize, null, null, debtors)
        }
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //val takenImage = data?.extras?.get("data") as Bitmap
            //val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
            //addReceiptImageView.setImageBitmap(takenImage)
            //addReceiptImageView.setImageURI(Uri.parse(photoPath))

            addReceiptImageView.rotation = 90f
            addReceiptImageView.setImageBitmap(data?.extras?.get("data") as Bitmap)
            //fotoUriPath = data.data!!
           // Log.i("uri", fotoUriPath.toString())
        } else if (requestCode == CHOOSE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            addReceiptImageView.rotation = 90f
            addReceiptImageView.setImageURI(data?.data)
            fotoUriPath = data?.data!!
            Log.i("uri", fotoUriPath.toString())
        } else if (requestCode == REQUEST_TAKE_FOTO && resultCode == Activity.RESULT_OK) {
            //addReceiptImageView.rotation = 90f
            //addReceiptImageView.setImageURI(Uri.parse(photoPath))
        }
        else {
            //Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDebtorsDialog() {
        val builder = AlertDialog.Builder(this)
        
        builder.setTitle("Select debtors")
        builder.setCancelable(false)
        for (user in usersList) {
            selectedDebtors.add(false)
        }

        builder.setMultiChoiceItems(usersList.toTypedArray(), selectedDebtors.toBooleanArray()) { dialog, which, isChecked ->
            // Update the current focused item's checked status
            selectedDebtors[which] = isChecked
            // Get the current focused item
            val currentItem = usersList[which]
            // Notify the current action
            Toast.makeText(applicationContext, "$currentItem $isChecked", Toast.LENGTH_SHORT).show()
        }
        // Set the positive/yes button click listener
        builder.setPositiveButton("OK") { dialog, which ->
            // Do something when click positive button
            //receiptDebtorsTextView.text = "Your preferred debtors..... \n"
            receiptDebtorsTextView.text = ""
            for (i in selectedDebtors.indices) {
                val checked = selectedDebtors[i]
                if (checked) {
                    receiptDebtorsTextView.text = receiptDebtorsTextView.text.toString() + usersList[i] + ", "
                }
            }
        }
        builder.setNeutralButton("Clear") { dialog, which ->
            for (i in selectedDebtors.indices) {
                selectedDebtors[i] = false
                receiptDebtorsTextView.text = ""
            }
        }
        val dialog = builder.create()
        // Display the alert dialog on interface
        dialog.show()
    }

    private fun getUsersName(): List<String> {
        var usersName = mutableListOf<String>()
        usersListViewModel.usersLiveData.value?.forEach { usersName.add(it.name.toString()) }
        return usersName
    }
}
