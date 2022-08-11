package com.openclassrooms.realestatemanager.provider.utils

import android.content.Context
import android.net.wifi.WifiManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UtilsTests {

    private lateinit var context : Context

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
    }

    @Test
    fun isInternetAvailableTest() {
        val wifi = context
            .getSystemService(Context.WIFI_SERVICE) as WifiManager
        assert(wifi.isWifiEnabled)
    }
}