package com.openclassrooms.realestatemanager.ui.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.ui.adapter.ViewPagerAdapter
import com.openclassrooms.realestatemanager.viewmodel.MainViewModel

class MainActivity : BaseActivity() {

    private lateinit var bottomNav : BottomNavigationView

    private lateinit var viewPager: ViewPager2

    private lateinit var viewModel: MainViewModel

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        bottomNav = binding.bottomNavigationViewActivityMain
        viewPager = binding.viewpagerActivityMain

        configBottomNav()
        configViewpager()
    }

    private fun configViewpager() {
        val pagerAdapter = ViewPagerAdapter(this)
        viewPager.adapter = pagerAdapter

        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                when(position) {
                    0 -> bottomNav.menu.getItem(0).isChecked = true
                    1 -> bottomNav.menu.getItem(1).isChecked = true
                    2 -> bottomNav.menu.getItem(2).isChecked = true
                }
                super.onPageSelected(position)
            }
        })
    }

    private fun configBottomNav() {
        bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home_bottom_navigation -> viewPager.currentItem = 0
                R.id.explorer_bottom_navigation -> viewPager.currentItem = 1
                R.id.profile_bottom_navigation -> viewPager.currentItem = 2
            }
            true
        }
    }
}