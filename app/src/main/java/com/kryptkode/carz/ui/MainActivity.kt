package com.kryptkode.carz.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.kryptkode.carz.R
import com.kryptkode.carz.navigator.NavComponentsProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavComponentsProvider {

    override val navController: NavController
        get() = (
            supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            ).navController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
