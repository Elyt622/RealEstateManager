package com.openclassrooms.realestatemanager.ui.activity

import androidx.appcompat.app.AppCompatActivity
import com.openclassrooms.realestatemanager.app.App
import io.reactivex.rxjava3.disposables.CompositeDisposable

open class BaseActivity : AppCompatActivity() {

    protected val bag = CompositeDisposable()

    val propertyDao = App.database.propertyDao()

    override fun onDestroy() {
        super.onDestroy()
        bag.clear()
    }
}