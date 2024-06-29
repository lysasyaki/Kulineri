package com.alysa.myrecipe.recipe

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alysa.myrecipe.R
import com.alysa.myrecipe.core.domain.Favorite.get.DataGet
import com.bumptech.glide.Glide

class FavoriteAdapter(
    private val context: Context,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(data: DataGet)
    }

    private val list: MutableList<DataGet> = mutableListOf()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_name: TextView = itemView.findViewById(R.id.tvName)
        var tv_desc: TextView = itemView.findViewById(R.id.tvResepDesc)
        var img_Resep: ImageView = itemView.findViewById(R.id.ivResep)
        var tv_kategori: TextView = itemView.findViewById(R.id.tvCategory)

        init {
            if (tv_kategori == null) {
                Log.e("ViewHolder", "tvKategori is null")
            }
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener.onItemClick(list[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.tv_kategori.visibility = View.GONE
        holder.tv_name.text = item.recipe?.name?.toUpperCase() ?: ""
        holder.tv_desc.apply {
            text = item.recipe?.description ?: ""
            maxLines = 5
            ellipsize = android.text.TextUtils.TruncateAt.END
        }

        val imageUrl = item.recipe?.image?.getOrNull(0)

        if (imageUrl != null && imageUrl.isNotBlank()) {
            Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.gambar_default)
                .error(R.drawable.gambar_default)
                .centerCrop()
                .into(holder.img_Resep)
        } else {
            Glide.with(context)
                .load(R.drawable.gambar_default) // Placeholder image or default image
                .centerCrop()
                .into(holder.img_Resep)
        }
    }

    fun updateData(newList: List<DataGet>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }


    fun clearData() {
        list.clear()
        notifyDataSetChanged()
    }

    fun addData(resep: DataGet) {
        list.add(resep)
        notifyDataSetChanged()
    }
}