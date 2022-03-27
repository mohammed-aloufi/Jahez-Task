package com.example.jahez_task.presentation.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jahez_task.R
import com.example.jahez_task.databinding.ResturantListItemBinding
import com.example.jahez_task.domain.models.Restaurant
import com.google.gson.annotations.SerializedName
import java.math.RoundingMode
import java.text.DecimalFormat

class RestaurantAdapter(private val restaurants: List<Restaurant>) :
    RecyclerView.Adapter<RestaurantViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val binding = ResturantListItemBinding.inflate(
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
class RestaurantViewHolder(private val binding: ResturantListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("ResourceAsColor")
    fun onBind(restaurant: Restaurant) {
        binding.restaurantNameTv.text = restaurant.name
        binding.hoursTxtView.text = restaurant.hours
        Glide.with(binding.root).load(restaurant.imageUrl).into(binding.restaurantImgView)
        binding.ratingTxtView.text = restaurant.rating.toString()
        binding.distanceTxtView.text = restaurant.distance.roundOffDecimal()
        if (restaurant.hasOffer){
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

    private fun Double.roundOffDecimal(): String {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.FLOOR
        return df.format(this).toString()
    }
}