package com.example.receiptapp.receipt

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.receiptapp.R
import com.example.receiptapp.product.AddProductActivity
import com.example.receiptapp.product.ProductListViewModelFactory
import com.example.receiptapp.product.ProductsListViewModel
import com.example.receiptapp.receiptData.Receipt
import com.example.receiptapp.receiptDetail.ReceiptDetailActivity
import com.example.receiptapp.settlements.SettlementsListViewModel
import com.example.receiptapp.settlements.SettlementsListViewModelFactory
import com.example.receiptapp.settlementsData.Settlement
import com.example.receiptapp.user.UsersListViewModel
import com.example.receiptapp.user.UsersListViewModelFactory
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.ArrayList
import kotlin.random.Random

const val RECEIPT_ID = "receipt id"
const val UPDATE_RECEIPT = 321

class ReceiptFragment : Fragment() {

    private val newReceiptActivityRequestCode = 1
    private val receiptsListViewModel by viewModels<ReceiptsListViewModel> {
        ReceiptsListViewModelFactory(requireContext())
    }
    private val settlementsListViewModel by viewModels<SettlementsListViewModel> {
        SettlementsListViewModelFactory(requireContext())
    }
    private val productsListViewModel by viewModels<ProductsListViewModel> {
        ProductListViewModelFactory(requireContext())
    }
    private val usersListViewModel by viewModels<UsersListViewModel> {
        UsersListViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_receipt, container, false)
        val receiptRecyclerView = view.findViewById<RecyclerView>(R.id.receipt_recycler_view)
        val receiptAdapter = ReceiptAdapter (
            { receipt: Receipt -> adapterOnClick(receipt) },
            { receipt ->  deleteClick(receipt) },
            { receipt -> settingsClick(receipt) })
        receiptRecyclerView.adapter = receiptAdapter
        receiptRecyclerView.apply {
            val swipeDelete = object: SwipeToDeleteCallback(){
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    receiptsListViewModel.removeReceiptId(viewHolder.itemId)
                    productsListViewModel.removeProductForReceiptId(viewHolder.itemId)
                    Toast.makeText(activity, "delete receipt clicked yeah", Toast.LENGTH_SHORT).show()
                    settlementsListViewModel.clearSettlements()
                    refreshSettlements()
                    /*receiptsListViewModel.getReceiptById(viewHolder.itemId)?.let {
                        receiptsListViewModel.removeReceipt(
                            it
                        )
                    }*/
                    //receiptAdapter.notifyItemRemoved(viewHolder.layoutPosition)
                    Toast.makeText(activity, "Add receipt swiped", Toast.LENGTH_SHORT).show()
                }
            }
            val touchHelper = ItemTouchHelper(swipeDelete)
            touchHelper.attachToRecyclerView(receiptRecyclerView)
        }

        receiptsListViewModel.receiptsLiveData.observe(viewLifecycleOwner) {
            it?.let {
                receiptAdapter.submitList(it as MutableList<Receipt>)
            }
        }

        val fab: View = view.findViewById(R.id.add_receipt_floating_action_button)
        fab.setOnClickListener {
            Toast.makeText(activity, "Add receipt clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, AddReceiptActivity::class.java)
            startActivityForResult(intent, newReceiptActivityRequestCode)
           /* val intent = Intent(activity, AddProductActivity::class.java)
            intent.putExtra(RECEIPT_ID, 1)
            startActivityForResult(intent, 1)*/
        }
        return view
    }

    private fun settingsClick(receipt: Receipt) {
        Toast.makeText(activity, "settings receipt clicked ${receipt.id}", Toast.LENGTH_SHORT).show()
    }

    private fun deleteClick(receipt: Receipt) {
        productsListViewModel.removeProductForReceiptId(receipt.id)
        //receiptsListViewModel.removeReceiptId(receipt.id)
        receiptsListViewModel.removeReceipt(receipt)
        //receiptsListViewModel.removeReceiptId(receipt.id)

        Toast.makeText(activity, "delete receipt", Toast.LENGTH_SHORT).show()
        settlementsListViewModel.clearSettlements()
        refreshSettlements(receipt.id)
    }

    private fun adapterOnClick(receipt: Receipt) {
        //Toast.makeText(activity, "Receipt clicked number " + receipt.id.toString(), Toast.LENGTH_SHORT).show()
        val intent = Intent(context, ReceiptDetailActivity::class.java)
        intent.putExtra(RECEIPT_ID, receipt.id)
        startActivity(intent)
    }

    private fun refreshSettlements(id: Long = -1) {
        settlementsListViewModel.clearSettlements()
        if (id.toInt() != -1) {
            receiptsListViewModel.removeReceiptId(id)
        }
        val graph = Graph()
        for (receipt in receiptsListViewModel.getReceiptList().value!!) {
            val productList = productsListViewModel.getProductListForReceiptId(receipt.id)
            var currentReceiptPrize = receipt.prize
            if (productList != null) {
                for (product in productList) {
                    val productPrize = product.prize * product.amount
                    currentReceiptPrize -= productPrize
                    //product.debtors?.forEach { graph.addEdge(it, receipt.owner, productPrize / product.debtors.size) }
                    for (debtor in product.debtors!!) {
                        graph.addEdge(debtor, receipt.owner, productPrize / product.debtors.size)
                    }
                }
            }
            //receipt.debtors?.forEach { graph.addEdge(it, receipt.owner, currentReceiptPrize / receipt.debtors.size) }
            for (debtor in receipt.debtors!!) {
                graph.addEdge(debtor, receipt.owner, currentReceiptPrize / receipt.debtors.size)
            }
        }
        graph.repairGraph()
        val settlementsList =  mutableListOf<Settlement>()
        for (edge in graph.getEdges()) {
            //settlementsListViewModel.insertSettlement(settlementSenderId = edge.begin, settlementSender = usersListViewModel.getNameForId(edge.begin), settlementReceiverId = edge.end, settlementReceiver = usersListViewModel.getNameForId(edge.end), settlementPrize = edge.value)
            if (edge.value != 0.0) {
                val newSettlement = Settlement(
                    id = Random.nextLong(),
                    senderId = edge.begin,
                    senderName = usersListViewModel.getNameForId(edge.begin),
                    receiverId = edge.end,
                    receiverName = usersListViewModel.getNameForId(edge.end),
                    prize = edge.value,
                    receipts = null
                )
                settlementsList.add(newSettlement)
            }
        }
        settlementsListViewModel.updateSettlements(settlementsList.toList())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == newReceiptActivityRequestCode && resultCode == Activity.RESULT_OK) {
            settlementsListViewModel.clearSettlements()
            refreshSettlements()

            /**
             * iterowac po paragonach i znajdowac proodukty tego paragonu
             * zmniejszac kwote paragonu o produkty, dzielic paragon na reszte i dodac do edges
             * potem dzielic produkt i dodac**/
        } else if (requestCode == UPDATE_RECEIPT && resultCode == Activity.RESULT_OK) {

        }
    }
}

class Edge(var begin: Long, var end: Long, var value: Double) {
    /*fun changeValue(diff: Double) {
        value += diff
    }

    fun reversed(otherEdge: Edge): Boolean {
        return begin == otherEdge.end && end == otherEdge.begin
    }

    fun equals(otherEdge: Edge): Boolean {
        return begin == otherEdge.begin && end == otherEdge.end
    }*/

    fun same(otherEdge: Edge): Boolean {
        return begin == otherEdge.begin && end == otherEdge.end || begin == otherEdge.end && end == otherEdge.begin
    }

    fun reversEdge() {
        val temp = begin
        begin = end
        end = temp
        value *= -1
    }

    fun concat(newEdge: Edge) {
        if (begin == newEdge.begin)
            value += newEdge.value
        else if (begin == newEdge.end)
            value -= newEdge.value
    }
}

class Graph() {
    private val edges = ArrayList<Edge>()

    fun addEdge(begin: Long, end: Long, value: Double) {
        val newEdge = Edge(begin, end, value)
        /*val edge = edges.let { edges ->
            edges.firstOrNull { it.same(newEdge) }
        }*/
        val edge = edges.firstOrNull { edge ->
            edge.same(newEdge)
        }
        if (edge == null) {
            edges.add(newEdge)
        } else {
            edge.concat(newEdge)
        }
    }

    fun repairGraph() {
        for (edge in edges) {
            if (edge.value < 0)
                edge.reversEdge()
        }
    }

    fun getEdges(): List<Edge> {
        return edges
    }
}