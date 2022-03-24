package com.example.jahez_task.presentation.home

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jahez_task.R
import com.example.jahez_task.databinding.HomeFragmentBinding
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

private const val TAG = "HomeFragment"

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: HomeFragmentBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(layoutInflater)
        val dummy = listOf(
            "1",
            "1",
            "1",
            "1",
            "1",
            "1",
            "1",
        )
        setOnScrollListener()
        binding.restaurantsRv.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = RestaurantAdapter(dummy)
        }
        
        binding.searchView.setOnTouchListener { view, motionEvent ->  
            if (motionEvent.action == MotionEvent.ACTION_DOWN){
                binding.searchView.isIconified = false
            }
            true
        }

        return binding.root
    }

    private fun setOnScrollListener(){
        binding.logoAppbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            when {
                abs(verticalOffset) == appBarLayout.totalScrollRange -> {
                    // Collapsed
                    binding.searchView.visibility = View.GONE
                    binding.sortImgView.visibility = View.GONE
                }
                verticalOffset == 0 -> {
                    // Expanded
                    binding.searchView.visibility = View.VISIBLE
                    binding.sortImgView.visibility = View.VISIBLE
                }
                else -> {
                    // Somewhere in between
                    binding.searchView.visibility = View.GONE
                    binding.sortImgView.visibility = View.GONE
                }
            }
        })
    }
}