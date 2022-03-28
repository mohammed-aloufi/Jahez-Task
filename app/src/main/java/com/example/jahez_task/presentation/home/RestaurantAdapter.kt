package com.example.jahez_task.presentation.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jahez_task.R
import com.example.jahez_task.databinding.RestaurantListItemBinding
import com.example.jahez_task.domain.models.Restaurant


class RestaurantAdapter(private val restaurants: List<Restaurant>) :
    RecyclerView.Adapter<RestaurantViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val binding = RestaurantListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RestaurantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant = restaurants[position]
        holder.onBind(restaurant)
    }

    override fun getItemCount(): Int = restaurants.size
}

class RestaurantViewHolder(private val binding: RestaurantListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("ResourceAsColor")
    fun onBind(restaurant: Restaurant) {
        binding.restaurantNameTv.text = restaurant.name
        binding.hoursTxtView.text = restaurant.hours
        Glide.with(binding.root).load(restaurant.imageUrl).into(binding.restaurantImgView)
        binding.ratingTxtView.text = restaurant.rating
        binding.distanceTxtView.text = restaurant.distance
        if (restaurant.hasOffer) {
            binding.offerView.visibility = View.VISIBLE
            binding.offersTxtView.visibility = View.VISIBLE
            (restaurant.offer.first() + " SR Delivery").also { binding.offersTxtView.text = it }
        }
        when {
            restaurant.name.length % 2 == 0 -> {
                binding.openCloseTxtView.text = "Open"
                binding.openCloseTxtView.setTextColor(binding.root.context.resources.getColor(R.color.green))
            }
            else -> {
                binding.openCloseTxtView.text = "Close"
                binding.openCloseTxtView.setTextColor(binding.root.context.resources.getColor(R.color.light_red))
            }
        }
    }
}