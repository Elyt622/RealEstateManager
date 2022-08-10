package com.openclassrooms.realestatemanager.ui.fragment

import androidx.fragment.app.Fragment
import com.openclassrooms.realestatemanager.app.App

open class BaseFragment : Fragment() {
    val propertyDao = App.database.propertyDao()
}