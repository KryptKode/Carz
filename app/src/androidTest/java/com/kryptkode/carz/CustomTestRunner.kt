package com.kryptkode.carz

import android.app.Application
import android.content.Context
import android.os.StrictMode
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

class CustomTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader, name: String, context: Context): Application {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}
