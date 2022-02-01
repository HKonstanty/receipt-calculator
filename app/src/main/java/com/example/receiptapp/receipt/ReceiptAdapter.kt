package com.example.receiptapp.receipt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.receiptapp.R
import com.example.receiptapp.receiptData.Receipt
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ReceiptAdapter(private val onClick: (Receipt) -> Unit, private val onClickDelete: (Receipt) -> Unit,
                     private val onClickSettings: (Receipt) -> Unit) :
    ListAdapter<Receipt, ReceiptAdapter.ReceiptViewHolder>(ReceiptDiffCallback) {


    class ReceiptViewHolder(itemView: View, val onClick: (Receipt) -> Unit, private val onClickDelete: (Receipt) -> Unit,
                            private val onClickSettings: (Receipt) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val receiptTitleTextView: TextView = itemView.findViewById(R.id.receipt_title)
        private val receiptPrizeTextView: TextView = itemView.findViewById(R.id.receipt_prize)
        private val receiptDebtorsAmountTextView: TextView = itemView.findViewById(R.id.receipt_debtors_amount)
        private val receiptProductsAmountTextView: TextView = itemView.findViewById(R.id.receipt_products_amount)
        private val receiptImageView: ImageView = itemView.findViewById(R.id.receipt_image)

       // private val receiptSettingsFloatingActionButton: ImageView = itemView.findViewById(R.id.receipt_settings_floating_action_button)
        private val receiptRemoveFloatingActionButton: ImageView = itemView.findViewById(R.id.receipt_remove_floating_action_button)

        private var currentReceipt: Receipt? = null

        init {
            itemView.setOnClickListener {
                currentReceipt?.let {
                    onClick(it)
                }
            }

            /*receiptSettingsFloatingActionButton.setOnClickListener {
                currentReceipt?.let {
                    onClickSettings(it)
                }
            }*/

            receiptRemoveFloatingActionButton.setOnClickListener {
                currentReceipt?.let {
                    onClickDelete(it)
                }
            }
        }

        fun bind(receipt: Receipt) {
            currentReceipt = receipt

            receiptTitleTextView.text = receipt.name
            receiptPrizeTextView.text = receipt.prize.toString() + " zł"

            if (receipt.debtors != null) {
                receiptDebtorsAmountTextView.text = receipt.debtors.size.toString()
            } else {
                receiptDebtorsAmountTextView.text = "0"
            }

            if (receipt.products != null) {
                receiptProductsAmountTextView.text = receipt.products.size.toString()
            } else {
                receiptProductsAmountTextView.text = "0"
            }

            if (receipt.image != null) {
                receiptImageView.setImageResource(receipt.image)
            } else {
                receiptImageView.setImageResource(R.drawable.ic_avatar)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.receipt_item, parent, false)
        return ReceiptViewHolder(view, onClick, onClickDelete, onClickSettings)
    }

    override fun onBindViewHolder(holder: ReceiptViewHolder, position: Int) {
        val receipt = getItem(position)
        holder.bind(receipt)
    }
}

object ReceiptDiffCallback : DiffUtil.ItemCallback<Receipt>() {
    override fun areItemsTheSame(oldItem: Receipt, newItem: Receipt): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Receipt, newItem: Receipt): Boolean {
        return oldItem.id == newItem.id
    }
}
