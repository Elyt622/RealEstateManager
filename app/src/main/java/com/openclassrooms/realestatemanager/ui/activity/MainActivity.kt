package com.openclassrooms.realestatemanager.ui.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.ui.adapter.ViewPagerAdapter
import com.openclassrooms.realestatemanager.viewmodel.MainViewModel

class MainActivity : BaseActivity() {

    private lateinit var viewModel: MainViewModel

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        configBottomNav()
        configViewpager()
    }

    private fun configViewpager() {
        val pagerAdapter = ViewPagerAdapter(this)
        with(binding) {
            viewpager.adapter = pagerAdapter
            viewpager.isUserInputEnabled = false
            viewpager.offscreenPageLimit = 2

            viewpager.registerOnPageChangeCallback(
                object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        when (position) {
                            0 -> bottomNavigationView.menu.getItem(0).isChecked = true
                            1 -> bottomNavigationView.menu.getItem(1).isChecked = true
                            2 -> bottomNavigationView.menu.getItem(2).isChecked = true
                        }
                        super.onPageSelected(position)
                    }
                }
            )
        }
    }

    private fun configBottomNav() {
        with(binding) {
            bottomNavigationView.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.home_bottom_navigation -> viewpager.currentItem = 0
                    R.id.map_bottom_navigation -> viewpager.currentItem = 1
                    R.id.profile_bottom_navigation -> viewpager.currentItem = 2
                }
                true
            }
        }
    }
}