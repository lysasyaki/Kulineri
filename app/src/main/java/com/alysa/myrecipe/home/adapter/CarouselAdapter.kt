package com.alysa.myrecipe.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.alysa.myrecipe.R
import com.alysa.myrecipe.home.presenter.slider

class CarouselAdapter(private val slides: List<slider>) :
    RecyclerView.Adapter<CarouselAdapter.slideViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): slideViewHolder {
        return slideViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_carousel,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return slides.size
    }

    override fun onBindViewHolder(holder: slideViewHolder, position: Int) {
        holder.bind(slides[position])
    }

    inner class slideViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val imageIcon = view.findViewById<ImageView>(R.id.ivCarousel)

        fun bind(slide: slider) {
            imageIcon.setImageResource(slide.Icon)
        }
    }
}