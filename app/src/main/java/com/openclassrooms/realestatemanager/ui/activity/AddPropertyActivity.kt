package com.openclassrooms.realestatemanager.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Option
import com.openclassrooms.realestatemanager.model.Type
import com.openclassrooms.realestatemanager.ui.adapter.OptionRvAdapterAddPropertyActivity
import com.openclassrooms.realestatemanager.ui.adapter.TypeRvAdapter

class AddPropertyActivity : AppCompatActivity() {

    private lateinit var topToolbar: Toolbar

    private lateinit var typeRv: RecyclerView

    private lateinit var optionRv: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_property)

        topToolbar = findViewById(R.id.toolbar_on_top_add_property_activity)
        typeRv = findViewById(R.id.recyclerview_type_add_property_activity)
        optionRv = findViewById(R.id.recyclerview_option_add_property_activity)

        configToolbar()
        configTypeRecyclerView()
        configOptionRecyclerView()
    }

    private fun configTypeRecyclerView() {
        typeRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        typeRv.adapter = TypeRvAdapter(this, Type.values())
    }

    private fun configOptionRecyclerView() {
        optionRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        optionRv.adapter = OptionRvAdapterAddPropertyActivity(this, Option.values())
    }

    private fun configToolbar() {
        setSupportActionBar(topToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}