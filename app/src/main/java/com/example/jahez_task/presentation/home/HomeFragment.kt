package com.example.jahez_task.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jahez_task.R
import com.example.jahez_task.base.BaseFragment
import com.example.jahez_task.databinding.HomeFragmentBinding
import com.example.jahez_task.domain.models.Restaurant
import com.example.jahez_task.utils.Constants.DISTANCE_SORT
import com.example.jahez_task.utils.Constants.RATING_SORT
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

private const val TAG = "HomeFragment"

@AndroidEntryPoint
class HomeFragment : BaseFragment(), SortBottomSheet.SortCallBack {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: HomeFragmentBinding
    private var restaurants: List<Restaurant> = emptyList()
    private var filteredRestaurants: List<Restaurant> = restaurants

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(layoutInflater)
        setBaseViewModel(viewModel)

        observeLoggedInState()
        observeRestaurant()
        setOnScrollListener()
        setRvLayoutManger()
        setSearchView()

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.sortImgView.setOnClickListener {
            val sortFragment = SortBottomSheet()
            sortFragment.setTargetFragment(this, 0)
            sortFragment.show(parentFragmentManager, viewModel.sortBy)
        }
    }

    private fun observeLoggedInState() {
        collectLatestLifecycleFlow(viewLifecycleOwner, viewModel.loggedState) {
            if (!it) findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            Log.d(TAG, "observeLoggedInState: $it")
        }
    }

    private fun setSearchView() {
        binding.searchView.doOnTextChanged { text, start, before, count ->
            filteredRestaurants = restaurants.filter {
                it.name.lowercase().contains(text.toString().lowercase())
            }
            binding.restaurantsRv.adapter = RestaurantAdapter(filteredRestaurants)
        }
    }


    private fun observeRestaurant() {
        collectLifecycleFlow(viewLifecycleOwner, viewModel.restaurants) { restaurants ->
            this.restaurants = restaurants
            filteredRestaurants = restaurants
            binding.restaurantsRv.adapter = RestaurantAdapter(filteredRestaurants)
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

    override fun onSortSelected(sortBy: String) {
        viewModel.sortBy = sortBy
        updateUI()
    }

    private fun updateUI() {
        val sortedList = when (viewModel.sortBy) {
            RATING_SORT -> {
                filteredRestaurants.sortedBy {
                    it.rating
                }
            }
            DISTANCE_SORT -> {
                filteredRestaurants.sortedBy {
                    it.distance
                }
            }
            else -> filteredRestaurants
        }

        binding.restaurantsRv.adapter = RestaurantAdapter(sortedList)
    }
}