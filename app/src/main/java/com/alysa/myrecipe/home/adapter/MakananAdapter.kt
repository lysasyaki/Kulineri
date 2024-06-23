package com.alysa.myrecipe.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alysa.myrecipe.R
import com.alysa.myrecipe.core.domain.recipe.Data
import com.bumptech.glide.Glide

class MakananAdapter (
    private val context: Context,
    private val itemClickListener: OnItemClickListener
    ) : RecyclerView.Adapter<MakananAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(data: Data)
    }

    private val list: MutableList<Data> = mutableListOf()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvResep: TextView = itemView.findViewById(R.id.tvResep)
        var imgResep: ImageView = itemView.findViewById(R.id.imgResep)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener.onItemClick(list[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(
            R.layout.item_card,
            parent,
            false
        )
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.tvResep.text = item?.name ?.toUpperCase() ?: ""

        // Ambil URL gambar dari list image
        val imageUrl = item.image?.getOrNull(0)

        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.gambar_default)
            .error(R.drawable.gambar_default)
            .centerCrop()
            .into(holder.imgResep)
    }

    fun updateData(newList: List<Data>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    fun clearData() {
        list.clear()
        notifyDataSetChanged()
    }

    fun addData(resep: Data) {
        list.add(resep)
//        list.clear()
        notifyDataSetChanged()
    }
}

//    private var makananList: List<Data>? = null
//
//    fun setData(makananList: List<Data>) {
//        this.makananList = makananList
//        notifyDataSetChanged()
//    }
//
//    fun getData(): List<Data>? {
//        return makananList
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val makanan = makananList?.get(position)
//        holder.tvName.text = makanan?.name
//
//        // Load image using Glide library
//        if (makanan?.image != null && makanan.image.isNotEmpty()) {
//            Glide.with(context)
//                .load(makanan.image[0]) // Assuming only one image URL is present
//                .placeholder(R.drawable.gambar_default) // Placeholder image
//                .error(R.drawable.gambar_default) // Error image if loading fails
//                .centerCrop()
//                .into(holder.imgResep)
//        } else {
//            holder.imgResep.setImageResource(R.drawable.gambar_default) // Default placeholder if no image URL
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return makananList?.size ?: 0
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val imgResep: ImageView = itemView.findViewById(R.id.imgResep)
//        val tvName: TextView = itemView.findViewById(R.id.tv_name)
//    }
//}