package com.example.receiptapp.settlements

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.receiptapp.R
import com.example.receiptapp.receipt.ReceiptAdapter
import com.example.receiptapp.receipt.ReceiptDiffCallback
import com.example.receiptapp.receiptData.Receipt
import com.example.receiptapp.settlementsData.Settlement
import com.google.android.material.textview.MaterialTextView

class SettlementAdapter : ListAdapter<Settlement, SettlementAdapter.SettlementViewHolder>(SettlementDiffCallback) {

    class SettlementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val settlementSender: MaterialTextView = itemView.findViewById(R.id.settlement_sender_name)
        private val settlementReceiver: TextView = itemView.findViewById(R.id.settlement_receiver_name)
        private val settlementPrize: TextView = itemView.findViewById(R.id.settlement_prize)

        private var currentSettlement: Settlement? = null

        fun bind(settlement: Settlement) {
            currentSettlement = settlement

            settlementSender.text = settlement.senderName
            settlementReceiver.text = settlement.receiverName
            settlementPrize.text = settlement.prize.toString()+"zł"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettlementViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.settlement_item, parent, false)
        return SettlementViewHolder(view)
    }

    override fun onBindViewHolder(holder: SettlementViewHolder, position: Int) {
        val settlement = getItem(position)
        holder.bind(settlement)
    }
}

object SettlementDiffCallback : DiffUtil.ItemCallback<Settlement>() {
    override fun areContentsTheSame(oldItem: Settlement, newItem: Settlement): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: Settlement, newItem: Settlement): Boolean {
        return oldItem.id == newItem.id
    }
}