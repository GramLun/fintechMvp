package com.moskofidi.fintech

import android.content.pm.ActivityInfo
import android.os.Bundle
import com.arellomobile.mvp.MvpActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.moskofidi.fintech.adapter.TabAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MvpActivity() {
    private lateinit var fragmentAdapter: TabAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        DevApplication.setConnectionListener(this)
        DevApplication.setNetworkState()

        viewPager.adapter = TabAdapter(this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text =
                when (position) {
                    0 -> "Последние"
                    1 -> "Лучшие"
                    else -> {
                        "Горячие"
                    }
                }
        }.attach()
    }

    override fun onResume() {
        super.onResume()
        DevApplication.regListener()
    }

    override fun onStop() {
        super.onStop()
        DevApplication.unregisterListener()
    }
}