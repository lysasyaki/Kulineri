package com.alysa.myrecipe.recipe

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alysa.myrecipe.R
import com.alysa.myrecipe.core.domain.recipe.byUser.DataByUser
import com.alysa.myrecipe.core.domain.recipe.delete.DataDelete
import com.bumptech.glide.Glide

class ResepkuAdapter (
    private val context: Context,
    private val itemClickListener: OnItemClickListener,
    private val deleteClickListener: OnDeleteClickListener
) : RecyclerView.Adapter<ResepkuAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(data: DataByUser)
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(data: DataByUser)
    }

    private val list: MutableList<DataByUser> = mutableListOf()
//    private val lists: MutableList<DataDelete> = mutableListOf()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_name: TextView = itemView.findViewById(R.id.tvName)
        var tv_desc: TextView = itemView.findViewById(R.id.tvResepDesc)
        var img_Resep: ImageView = itemView.findViewById(R.id.ivResep)
        var tv_kategori: TextView = itemView.findViewById(R.id.tvCategory)
        var btn_hapus: FrameLayout = itemView.findViewById(R.id.btn_resepku)

        init {
            btn_hapus.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val data = list[position]
                    deleteClickListener.onDeleteClick(data)
                }
            }

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
        holder.tv_name.text = item.name?.toUpperCase() ?: ""
        holder.tv_desc.apply {
            text = item.description ?: ""
            maxLines = 5
            ellipsize = android.text.TextUtils.TruncateAt.END
        }

        val imageUrl = item.image?.getOrNull(0)

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
        Log.d("ResepkuAdapter", "Binding item at position $position: ${item.name}")
    }

    fun updateData(newList: List<DataByUser>) {
        Log.d("ResepkuAdapter", "Updating data with new list of size: ${newList.size}")
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }


    fun clearData() {
        list.clear()
        notifyDataSetChanged()
    }

    fun addData(resep: DataByUser) {
        list.add(resep)
        notifyDataSetChanged()
    }
}