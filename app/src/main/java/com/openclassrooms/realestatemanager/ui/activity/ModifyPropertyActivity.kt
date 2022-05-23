package com.openclassrooms.realestatemanager.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.openclassrooms.realestatemanager.databinding.ActivityModifyPropertyBinding

class ModifyPropertyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityModifyPropertyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModifyPropertyBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}