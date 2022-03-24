package com.example.jahez_task.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jahez_task.databinding.ResturantListItemBinding

class RestaurantAdapter(private val restaurants: List<String>) : RecyclerView.Adapter<RestaurantViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val binding = ResturantListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RestaurantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
//        val restaurant = restaurants[position]
        holder.onBind("restaurant")
    }

    override fun getItemCount(): Int = 30
}

class RestaurantViewHolder(private val binding: ResturantListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

        fun onBind(restaurant: String){

        }
}