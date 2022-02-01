package com.example.receiptapp.product

import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.receiptapp.R
import com.example.receiptapp.productData.Product
import androidx.lifecycle.observe
import com.example.receiptapp.receipt.RECEIPT_FOTO
import com.example.receiptapp.receipt.RECEIPT_ID
import com.example.receiptapp.user.UsersListViewModel
import com.example.receiptapp.user.UsersListViewModelFactory
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

const val PRODUCT_ID = "product id"

class AddProductActivity : AppCompatActivity() {

    private val usersListViewModel by viewModels<UsersListViewModel> {
        UsersListViewModelFactory(this)
    }
    private val productsListViewModel by viewModels<ProductsListViewModel> {
        ProductListViewModelFactory(this)
    }

    //private val newProductActivityRequestCode = 1
    //private lateinit var productToolbar: Toolbar
    private lateinit var addProductButton: FloatingActionButton
    private lateinit var selectedDebtors: MutableList<Boolean>
    private lateinit var usersList: List<String>
    private lateinit var receiptImage: ImageView
    //private lateinit var backButton: FloatingActionButton
    private var receipId: Long = -1
    private lateinit var receiptFotoPath: Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_product_activity)
        usersList = getUsersName()
        //productToolbar = findViewById(R.id.product_toolbar)
        addProductButton = findViewById(R.id.product_add_product)
        receiptImage = findViewById(R.id.add_product_image)
        //receiptFotoPath = intent.getParcelableExtra<Uri>(RECEIPT_FOTO)!!
        //Log.i("uri", receiptFotoPath.toString())
        //receiptImage.setImageURI(receiptFotoPath)
        addProductButton.setOnClickListener { showAddProductDialog() }
//       setSupportActionBar(productToolbar)
        val rv = findViewById<RecyclerView>(R.id.add_product_recycler_view)
        val productAdapter = ProductAdapter()
        rv.adapter = productAdapter

        productsListViewModel.productsLiveData.observe(this) {
            it.let { productAdapter.submitList(it as MutableList<Product>) }
        }

        receipId = intent.getLongExtra(RECEIPT_ID, -1)
        Log.i("Receipt id in add product", receipId.toString())
    }

    private fun showAddProductDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.add_product_dialog, null)
        val productDebtors = mDialogView.findViewById<TextView>(R.id.product_debtors)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("Add product")

        val mAlertDialog = mBuilder
        val dialog = mBuilder.create()
        // Display the alert dialog on interface
        dialog.show()

        productDebtors.setOnClickListener {
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Select debtors")
            builder.setCancelable(false)
            selectedDebtors = mutableListOf()
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
            builder.setPositiveButton("OK") { dialog, which ->
                // Do something when click positive button
                //receiptDebtorsTextView.text = "Your preferred debtors..... \n"
                productDebtors.text = ""
                for (i in selectedDebtors.indices) {
                    val checked = selectedDebtors[i]
                    if (checked) {
                        productDebtors.text = productDebtors.text.toString() + usersList[i] + ", "
                    }
                }
            }
            builder.setNeutralButton("Clear") { dialog, which ->
                for (i in selectedDebtors.indices) {
                    selectedDebtors[i] = false
                    productDebtors.text = ""
                }
            }
            val dialog = builder.create()
            // Display the alert dialog on interface
            dialog.show()
        }
        mDialogView.findViewById<MaterialButton>(R.id.save_product_button).setOnClickListener {
            dialog.dismiss()

            val productTitle =
                mDialogView.findViewById<TextInputEditText>(R.id.product_name).text.toString()
            val productPrize =
                mDialogView.findViewById<TextInputEditText>(R.id.product_prize_edit_text).text.toString()
                    .toDouble()
            val productAmount =
                mDialogView.findViewById<TextInputEditText>(R.id.product_amount_edit_text).text.toString()
                    .toInt()
            val debtors = mutableListOf<Long>()
            for (i in selectedDebtors.indices) {
                if (selectedDebtors[i]) {
                    usersListViewModel.getIdForName(usersList[i])?.let { debtors.add(it) }
                }
            }
            Log.i("product name", productTitle)
            Log.i("product amount", productAmount.toString())
            Log.i("product receipt id", receipId.toString())
            productsListViewModel.insertProduct(
                productTitle, receipId, productAmount, productPrize,
                debtors
            )

            /*if (selectedDebtors.size == 0 || mDialogView.findViewById<TextInputEditText>(R.id.product_name).text.isNullOrEmpty()
                || mDialogView.findViewById<TextInputEditText>(R.id.product_prize_edit_text).text.isNullOrEmpty() || mDialogView.findViewById<TextInputEditText>(
                    R.id.product_amount_edit_text
                ).text.isNullOrEmpty()
            ) {
                Toast.makeText(this, "Complete information", Toast.LENGTH_SHORT).show()
            } else {

                val productTitle =
                    mDialogView.findViewById<TextInputEditText>(R.id.product_name).text.toString()
                val productPrize =
                    mDialogView.findViewById<TextInputEditText>(R.id.product_prize_edit_text).text.toString()
                        .toDouble()
                val productAmount =
                    mDialogView.findViewById<TextInputEditText>(R.id.product_amount_edit_text).text.toString()
                        .toInt()
                val debtors = mutableListOf<Long>()
                for (i in selectedDebtors.indices) {
                    if (selectedDebtors[i]) {
                        usersListViewModel.getIdForName(usersList[i])?.let { debtors.add(it) }
                    }
                }
                Log.i("product name", productTitle)
                Log.i("product amount", productAmount.toString())
                Log.i("product receipt id", receipId.toString())
                productsListViewModel.insertProduct(
                    productTitle, receipId, productAmount, productPrize,
                    debtors
                )
            }*/
        }
        mDialogView.findViewById<MaterialButton>(R.id.cancel_product_button).setOnClickListener{
            dialog.dismiss()
        }
    }

    private fun getUsersName(): List<String> {
        var usersName = mutableListOf<String>()
        usersListViewModel.usersLiveData.value?.forEach { usersName.add(it.name.toString()) }
        return usersName
    }
}