package com.example.jahez_task.presentation.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jahez_task.R
import com.example.jahez_task.base.BaseFragment
import com.example.jahez_task.databinding.HomeFragmentBinding
import com.example.jahez_task.domain.models.Restaurant
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

private const val TAG = "HomeFragment"

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: HomeFragmentBinding
    private var restaurants: List<Restaurant> = emptyList()
    private var filteredRestaurants: List<Restaurant> = restaurants

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(layoutInflater)

        observeLoggedInState()
        observeRestaurant()
        setOnScrollListener()
        setRvLayoutManger()
        setSearchView()

        return binding.root
    }

    private fun observeLoggedInState(){
        collectLatestLifecycleFlow(viewLifecycleOwner, viewModel.loggedState){
            if (!it) findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            Log.d(TAG, "observeLoggedInState: $it")
        }
    }

    private fun setSearchView(){
        setSearchViewOnTouchListener()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                filteredRestaurants = restaurants.filter {
                    it.name.lowercase().contains(p0.toString().lowercase())
                }
                binding.restaurantsRv.adapter = RestaurantAdapter(filteredRestaurants)
                return true
            }

        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setSearchViewOnTouchListener(){
        binding.searchView.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                binding.searchView.isIconified = false
            }
            true
        }
    }

    private fun observeRestaurant() {
        collectLifecycleFlow(viewLifecycleOwner, viewModel.restaurantsState) { state ->
            when {
                state.isLoading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.errorTxtView.visibility = View.GONE
                }
                state.restaurants.isNotEmpty() -> {
                    restaurants = state.restaurants
                    filteredRestaurants = restaurants
                    binding.progressBar.visibility = View.GONE
                    binding.errorTxtView.visibility = View.GONE
                    binding.restaurantsRv.adapter = RestaurantAdapter(filteredRestaurants)
                }
                state.message.isNotBlank() -> {
                    binding.progressBar.visibility = View.GONE
                    binding.errorTxtView.visibility = View.VISIBLE
                    binding.errorTxtView.text = state.message
                    Log.d(TAG, "observeRestaurant: ${state.message}")
                }
                else -> {
                    binding.progressBar.visibility = View.GONE
                    binding.errorTxtView.visibility = View.VISIBLE
                    binding.errorTxtView.text = getString(R.string.error_label)
                }
            }
        }
    }

    private fun setRvLayoutManger() {
        binding.restaurantsRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun setOnScrollListener() {
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