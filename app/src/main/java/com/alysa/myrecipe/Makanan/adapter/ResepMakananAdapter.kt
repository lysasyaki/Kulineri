package com.alysa.myrecipe.Makanan.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alysa.myrecipe.R
import com.alysa.myrecipe.core.domain.recipe.makanan.DataItem
import com.bumptech.glide.Glide

class ResepMakananAdapter (
    private val context: Context,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ResepMakananAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(data: DataItem)
    }

    private val list: MutableList<DataItem> = mutableListOf()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_category: TextView = itemView.findViewById(R.id.tvCategory)
        var tv_name: TextView = itemView.findViewById(R.id.tvName)
        var tv_desc: TextView = itemView.findViewById(R.id.tvResepDesc)
        var img_Resep: ImageView = itemView.findViewById(R.id.ivResep)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener.onItemClick(list[position])
                }
            }
            Log.d("ViewHolder", "tvCategory: $tv_category, tvName: $tv_name, tvDesc: $tv_desc, imgResep: $img_Resep")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(
            R.layout.item_recipe,
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

        holder.tv_name.text = item?.name ?.toUpperCase() ?: ""
        holder.tv_desc.apply {
            text = item?.description ?: ""
            maxLines = 3
            ellipsize = android.text.TextUtils.TruncateAt.END
        }
        holder.tv_category.text = item?.category?.name?:""

        // Ambil URL gambar dari list image
        val imageUrl = item.image?.getOrNull(0)

        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.gambar_default)
            .error(R.drawable.gambar_default)
            .centerCrop()
            .into(holder.img_Resep)
    }

    fun updateData(newList: List<DataItem>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    fun clearData() {
        list.clear()
        notifyDataSetChanged()
    }

    fun addData(resep: DataItem) {
        list.add(resep)
//        list.clear()
        notifyDataSetChanged()
    }
}