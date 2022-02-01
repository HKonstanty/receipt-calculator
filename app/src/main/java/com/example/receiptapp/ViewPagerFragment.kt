package com.example.receiptapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.receiptapp.receipt.ReceiptFragment
import com.example.receiptapp.settlements.SettlementsFragment
import com.example.receiptapp.user.UserFragment
import com.google.android.material.tabs.TabLayoutMediator


class ViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)

        val fragmentList = arrayListOf<Fragment>(
            ReceiptFragment(),
            UserFragment(),
            SettlementsFragment()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        view.findViewById<ViewPager2>(R.id.viewPager2).adapter = adapter
        TabLayoutMediator(view.findViewById(R.id.tabLayout), view.findViewById(R.id.viewPager2)) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Receipt"
                }
                1 -> {
                    tab.text = "Users"
                }
                 2 -> {
                     tab.text = "Settlements"
                 }
            }
        }.attach()
        return view
    }
}