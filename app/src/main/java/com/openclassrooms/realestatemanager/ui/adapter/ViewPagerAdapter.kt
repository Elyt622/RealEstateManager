package com.openclassrooms.realestatemanager.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.openclassrooms.realestatemanager.ui.fragment.HomeFragment
import com.openclassrooms.realestatemanager.ui.fragment.MapFragment
import com.openclassrooms.realestatemanager.ui.fragment.ProfileFragment

class ViewPagerAdapter(fa: FragmentActivity)
    : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return HomeFragment.newInstance()
            1 -> return MapFragment.newInstance()
            2 -> return ProfileFragment.newInstance()
        }
        return HomeFragment.newInstance()
    }
}