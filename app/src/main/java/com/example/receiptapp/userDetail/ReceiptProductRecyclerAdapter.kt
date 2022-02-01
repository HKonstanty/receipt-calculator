package com.example.receiptapp.userDetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.receiptapp.R
import com.example.receiptapp.productData.Product

class ReceiptProductRecyclerAdapter(private val products: List<Product>) : RecyclerView.Adapter<ReceiptProductRecyclerAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productTitle: TextView = itemView.findViewById(R.id.product_name)
        val productOwners: TextView = itemView.findViewById(R.id.product_owners)
        val productPrize: TextView = itemView.findViewById(R.id.product_prize)
        val productAmount: TextView = itemView.findViewById(R.id.product_amount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.productTitle.text = products[position].name
        holder.productOwners.text = products[position].debtors?.size.toString()
        holder.productPrize.text = products[position].prize.toString()+"zł"
        holder.productAmount.text = products[position].amount.toString()
    }

    override fun getItemCount(): Int {
        return products.size
    }
}