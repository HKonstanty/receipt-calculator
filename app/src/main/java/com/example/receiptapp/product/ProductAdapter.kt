package com.example.receiptapp.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.receiptapp.R
import com.example.receiptapp.productData.Product

class ProductAdapter() : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback) {

    class ProductViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val productNameTextView: TextView = itemView.findViewById(R.id.product_name)
        private val productDebtorsTextView: TextView = itemView.findViewById(R.id.product_owners)
        private val productPrizeTextView: TextView = itemView.findViewById(R.id.product_prize)
        private val productAmountTextView: TextView = itemView.findViewById(R.id.product_amount)

        private var currentProduct: Product? = null

        fun bind(product: Product) {
            currentProduct = product

            productNameTextView.text = product.name
            productPrizeTextView.text = product.prize.toString() + "zł"
            productAmountTextView.text = product.amount.toString()
            if (product.debtors != null) {
                productDebtorsTextView.text = product.debtors.size.toString()
            } else {
                productDebtorsTextView.text = "0"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }
}

object ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }
}
