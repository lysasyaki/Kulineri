package com.alysa.myrecipe.recipe.detail.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alysa.myrecipe.R
import com.alysa.myrecipe.core.domain.recipe.makanan.DataItem
import com.alysa.myrecipe.core.utils.RealmManager
import com.alysa.myrecipe.core.utils.ResultState
import com.alysa.myrecipe.core.utils.SpacesItemDecoration
import com.alysa.myrecipe.core.view.RecipeMakananView
import com.alysa.myrecipe.recipe.detail.presenter.DetailPresenter
import com.bumptech.glide.Glide
import io.realm.Realm

class DetailActivity : AppCompatActivity(), RecipeMakananView {

    private lateinit var presenter: DetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        Realm.init(this)
        RealmManager.initRealm()

        val btnBack = findViewById<ImageView>(R.id.btnBack)

        presenter = DetailPresenter()
        val uniqueId = intent.getIntExtra("id", 0)
        val dataItem = presenter.getDataByIdFromRealm(uniqueId)

        val tvRecipe = findViewById<TextView>(R.id.tvRecipeName)
        tvRecipe.text = dataItem?.name?.toUpperCase() ?:""

        val tvName = findViewById<TextView>(R.id.tvName)
        tvName.text = dataItem?.user?. name ?:""

        val tv_desc = findViewById<TextView>(R.id.tvDesc)
        tv_desc.text = dataItem?.description ?: ""

        val tv_bahan = findViewById<TextView>(R.id.tvBahan)
        tv_bahan.text = dataItem?.ingredient ?:""

        val tv_langkah = findViewById<TextView>(R.id.tvLangkah)
        tv_langkah.text = dataItem?.instruction ?: ""

        var imgResep = findViewById<ImageView>(R.id.ivRecipe)

        val imageUrl = dataItem?.image?.getOrNull(0)

        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.gambar_default)
            .error(R.drawable.gambar_default)
            .centerCrop()
            .into(imgResep)

        btnBack.setOnClickListener {
            onBackPressed()
        }

        val btnFavorite = findViewById<ImageView>(R.id.btnFavorite)

        btnFavorite.setOnClickListener {
            // Handle logic for favorite button click
            // Misalnya, panggil metode untuk menambahkan resep ini ke daftar favorit
//            addToFavorites(dataItem)
        }
    }

    private fun initRecyclerView(adapter: RecyclerView.Adapter<*>, recyclerView: RecyclerView) {
        val spanCount = 5
        recyclerView.layoutManager = GridLayoutManager(this, spanCount, GridLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(SpacesItemDecoration(6))
    }

    override fun displayRecipe(result: ResultState<List<DataItem>?>) {
        when (result) {
        is ResultState.Success -> {
                // Handle data berhasil diterima
                val productData = result.data

            }
            is ResultState.Error -> {
                // Handle jika terjadi error
                val errorMessage = result.error
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
            is ResultState.Loading -> {
                // Handle loading state
                Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
