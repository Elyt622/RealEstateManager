package com.openclassrooms.realestatemanager.ui.activity

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.adapter.ViewPagerAdapter
import com.openclassrooms.realestatemanager.viewmodel.MainViewModel

class MainActivity : BaseActivity() {

    private lateinit var toolbar: Toolbar

    private lateinit var fragment: FragmentContainerView

    private lateinit var bottomNav : BottomNavigationView

    private lateinit var viewPager: ViewPager2

    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        toolbar = findViewById(R.id.toolbar_main_activity)
        fragment = findViewById(R.id.fragment_activity_main)
        bottomNav = findViewById(R.id.bottom_navigation_view_activity_main)
        viewPager = findViewById(R.id.viewpager_activity_main)

        configToolbar()
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

    private fun configToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setLogo(R.drawable.logo_toolbar)
    }

}