package com.example.jahez_task.base

import android.annotation.SuppressLint
import android.app.Application
import dagger.hilt.android.HiltAndroidApp


private const val TAG = "App"

@HiltAndroidApp
class App: Application()