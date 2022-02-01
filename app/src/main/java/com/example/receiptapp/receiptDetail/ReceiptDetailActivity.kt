package com.example.receiptapp.receiptDetail

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.receiptapp.R
import com.example.receiptapp.product.ProductListViewModelFactory
import com.example.receiptapp.product.ProductsListViewModel
import com.example.receiptapp.productData.Product
import com.example.receiptapp.receipt.RECEIPT_ID
import com.example.receiptapp.receipt.ReceiptsListViewModel
import com.example.receiptapp.receipt.ReceiptsListViewModelFactory
import com.example.receiptapp.receiptData.Receipt
import com.example.receiptapp.user.UsersListViewModel
import com.example.receiptapp.user.UsersListViewModelFactory
import com.example.receiptapp.userDetail.ReceiptProductRecyclerAdapter
import com.google.android.material.textfield.TextInputEditText

class ReceiptDetailActivity : AppCompatActivity() {

    private val productsListViewModel by viewModels<ProductsListViewModel> {
        ProductListViewModelFactory(this)
    }
    private val receiptsListViewModel by viewModels<ReceiptsListViewModel> {
        ReceiptsListViewModelFactory(this)
    }
    private val usersListViewModel by viewModels<UsersListViewModel> {
        UsersListViewModelFactory(this)
    }

    private var selectedDebtors = mutableListOf<Boolean>()
    private lateinit var receiptTitle: TextView
    private lateinit var receiptImage: ImageView
    private lateinit var receiptOwner: TextInputEditText
    private lateinit var receiptPrize: TextInputEditText
    private lateinit var receiptDebtors: AutoCompleteTextView
    private lateinit var productRecycler: RecyclerView
    private lateinit var receiptDetailSave: Button
    private lateinit var currentReceipt: Receipt
    private var currentReceiptId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.receipt_detail_activity)

        receiptTitle = findViewById(R.id.receipt_detail_title)
        receiptOwner = findViewById(R.id.receipt_detail_owner)
        receiptDebtors = findViewById(R.id.receipt_detail_debtors)
        receiptPrize = findViewById(R.id.receipt_detail_prize)
        productRecycler = findViewById(R.id.receipt_product_recycler_view)
        //receiptDetailSave = findViewById(R.id.receipt_detail_save)

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            currentReceiptId = bundle.getLong(RECEIPT_ID)
        }
        currentReceipt = receiptsListViewModel.getReceiptById(currentReceiptId!!)!!
        currentReceiptId?.let {

            val products: List<Product>? = productsListViewModel.getProductListForReceiptId(currentReceiptId!!)

            receiptTitle.text = currentReceipt?.name
            receiptOwner.text = usersListViewModel.getNameForId(currentReceipt!!.owner).toEditable()
            var debtors = ""
            if (currentReceipt.debtors != null) {
                for (debtor in currentReceipt.debtors!!) {
                    debtors += usersListViewModel.getNameForId(debtor)+", "
                }
            }
            receiptDebtors.text = debtors.toEditable()
            receiptPrize.text = (currentReceipt.prize.toString() + "zł").toEditable()
            if (products != null) {
                productRecycler.adapter = ReceiptProductRecyclerAdapter(products)
                productRecycler.layoutManager = LinearLayoutManager(this)
                productRecycler.setHasFixedSize(true)
            }
        }

        //receiptDetailSave.setOnClickListener { updateReceipt() }
    }

    private fun updateReceipt() {
        val resultIntent = Intent()
        setResult(Activity.RESULT_OK, resultIntent)
        receiptsListViewModel.removeReceiptId(currentReceiptId!!)
        if (receiptTitle.text.isNullOrEmpty() || receiptPrize.text.isNullOrEmpty() || receiptOwner.text.isNullOrEmpty()) {
            Toast.makeText(this, "Complete all info", Toast.LENGTH_SHORT).show()
        } else {
            val title = receiptTitle.text.toString()
            val prize = receiptPrize.text.toString().substring(0, receiptPrize.text.toString().length-2).toDouble()
            Log.i("prize", prize.toString())
            val owner = receiptOwner.text.toString()
            val ownerId = usersListViewModel.getIdForName(owner)
            val debtors = mutableListOf<Long>()
            val usersList = getUsersName()
            for (i in selectedDebtors.indices) {
                if (selectedDebtors[i]) {
                    usersListViewModel.getIdForName(usersList[i])?.let { debtors.add(it) }
                }
            }

            receiptsListViewModel.insertReceipt(currentReceiptId!!, title, ownerId, prize, null, null, debtors)

        }
        finish()
    }

    private fun showDebtorsDialog() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Select debtors")
        builder.setCancelable(false)
        val usersList = getUsersName()
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

            var debtors = ""
            for (i in selectedDebtors.indices) {
                val checked = selectedDebtors[i]
                if (checked) {
                    debtors += usersList[i] + ", "
                }
            }
            receiptDebtors.text = debtors.toEditable()
        }
        builder.setNeutralButton("Clear") { dialog, which ->
            for (i in selectedDebtors.indices) {
                selectedDebtors[i] = false
                receiptDebtors.text = "".toEditable()
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

    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
}