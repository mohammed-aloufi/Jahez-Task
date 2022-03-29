package com.example.jahez_task.base

import android.os.Build
import android.os.Bundle
import android.os.Message
import android.view.View
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.jahez_task.R
import com.example.jahez_task.databinding.ActivityMainBinding
import com.example.jahez_task.domain.models.State
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val viewModel by viewModels<BaseViewModel>()
    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        findNavController(R.id.nav_host_fragment)

        setStatusBar()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun setStatusBar() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.insetsController?.setSystemBarsAppearance(
            APPEARANCE_LIGHT_STATUS_BARS,
            APPEARANCE_LIGHT_STATUS_BARS
        )
        (this as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    fun showProgress(show: Boolean) {
        if (show) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    fun showErrorMsg(show: Boolean, message: String = "") {
        if (show) {
            binding.errorTxtView.visibility = View.VISIBLE
            binding.errorTxtView.text = message
        } else {
            binding.errorTxtView.visibility = View.GONE
        }
    }

    fun showSnackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(
                resources.getColor(R.color.light_red, resources.newTheme())
            )
            .setTextColor(
                resources.getColor(R.color.white, resources.newTheme())
            ).show()
    }
}