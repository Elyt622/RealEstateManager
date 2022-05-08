package com.openclassrooms.realestatemanager.ui.activity

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable

open class BaseActivity : AppCompatActivity() {

    protected val bag = CompositeDisposable()

    override fun onDestroy() {
        super.onDestroy()
        bag.clear()
    }
}