//package com.alysa.myrecipe.recipe.detail.view
//
//import android.content.Context
//import android.content.SharedPreferences
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.widget.ImageView
//import android.widget.TextView
//import android.widget.Toast
//import com.alysa.myrecipe.R
//import com.alysa.myrecipe.core.domain.recipe.makanan.DataItem
//import com.alysa.myrecipe.core.utils.UserDataStoreImpl
//import com.alysa.myrecipe.recipe.detail.presenter.DetailPresenter
//import com.alysa.myrecipe.recipe.detail.presenter.FavoritesPresenter
//import com.bumptech.glide.Glide
//
//class DetailActivity : AppCompatActivity() {
//
//    private lateinit var presenter: DetailPresenter
//    private lateinit var favorites: FavoritesPresenter
//    private lateinit var userDataStoreImpl: UserDataStoreImpl
//    private lateinit var btnFavorite: ImageView
//    private lateinit var sharedPreferences: SharedPreferences
//    private var dataItem: DataItem? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_detail)
//
//        userDataStoreImpl = UserDataStoreImpl(this)
//        presenter = DetailPresenter()
//        favorites = FavoritesPresenter(this, userDataStoreImpl)
//        sharedPreferences = getSharedPreferences("favorite_prefs", Context.MODE_PRIVATE)
//
//        val uniqueId = intent.getIntExtra("id", 0)
//        dataItem = presenter.getDataByIdFromRealm(uniqueId)
//
//        setupUI()
//
//        btnFavorite.setOnClickListener {
//            dataItem?.let {
//                if (isFavorite(it.id)) {
//                    // Jika sudah disimpan (tombol berwarna merah), lakukan penghapusan
//                    favorites.deleteFavorite(it.id) { success ->
//                        runOnUiThread {
//                            if (success) {
//                                Toast.makeText(this, "Resep berhasil dihapus dari favorit", Toast.LENGTH_SHORT).show()
//                                // Hapus status dari SharedPreferences
//                                removeFavorite(it.id)
//                                // Update UI setelah penghapusan dari favorit
//                                updateFavoriteButton(false)
//                            } else {
//                                Toast.makeText(this, "Gagal menghapus resep dari favorit", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    }
//                } else {
//                    // Jika belum disimpan (tombol putih), tambahkan ke favorit
//                    favorites.addFavorite(it.id) { success ->
//                        runOnUiThread {
//                            if (success) {
//                                Toast.makeText(this, "Resep berhasil disimpan ke favorit", Toast.LENGTH_SHORT).show()
//                                // Simpan status di SharedPreferences
//                                saveFavorite(it.id)
//                                // Update UI setelah penambahan ke favorit
//                                updateFavoriteButton(true)
//                            } else {
//                                Toast.makeText(this, "Gagal menyimpan resep ke favorit", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private fun setupUI() {
//        val tvRecipe = findViewById<TextView>(R.id.tvRecipeName)
//        tvRecipe.text = dataItem?.name?.toUpperCase() ?: ""
//
//        val tvName = findViewById<TextView>(R.id.tvName)
//        tvName.text = dataItem?.user?.name ?: ""
//
//        val tv_desc = findViewById<TextView>(R.id.tvDesc)
//        tv_desc.text = dataItem?.description ?: ""
//
//        val tv_bahan = findViewById<TextView>(R.id.tvBahan)
//        tv_bahan.text = dataItem?.ingredient ?: ""
//
//        val tv_langkah = findViewById<TextView>(R.id.tvLangkah)
//        tv_langkah.text = dataItem?.instruction ?: ""
//
//        val imgResep = findViewById<ImageView>(R.id.ivRecipe)
//
//        val imageUrl = dataItem?.image?.getOrNull(0)
//
//        Glide.with(this)
//            .load(imageUrl)
//            .placeholder(R.drawable.gambar_default)
//            .error(R.drawable.gambar_default)
//            .centerCrop()
//            .into(imgResep)
//
//        btnFavorite = findViewById(R.id.btnFavorite)
//        updateFavoriteButton(isFavorite(dataItem?.id ?: 0))
//    }
//
//    private fun saveFavorite(recipeId: Int) {
//        sharedPreferences.edit().putBoolean("favorite_$recipeId", true).apply()
//    }
//
//    private fun removeFavorite(recipeId: Int) {
//        sharedPreferences.edit().remove("favorite_$recipeId").apply()
//    }
//
//    private fun isFavorite(recipeId: Int): Boolean {
//        return sharedPreferences.getBoolean("favorite_$recipeId", false)
//    }
//
//    private fun updateFavoriteButton(isFavorite: Boolean) {
//        if (isFavorite) {
//            // Set button color to red or any desired color for saved state
//            btnFavorite.setImageResource(R.drawable.icon_favorite_red) // Example icon change
//        } else {
//            // Set button color to white or any desired color for unsaved state
//            btnFavorite.setImageResource(R.drawable.icon_favorite_white) // Example icon change
//        }
//    }
//}


package com.alysa.myrecipe.recipe.detail.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.alysa.myrecipe.R
import com.alysa.myrecipe.core.domain.recipe.makanan.DataItem
import com.alysa.myrecipe.core.utils.RealmManager
import com.alysa.myrecipe.core.utils.ResultState
import com.alysa.myrecipe.core.utils.UserDataStoreImpl
import com.alysa.myrecipe.core.view.RecipeMakananView
import com.alysa.myrecipe.recipe.detail.presenter.DetailPresenter
import com.alysa.myrecipe.recipe.detail.presenter.FavoritesPresenter
import com.bumptech.glide.Glide
import io.realm.Realm

class DetailActivity : AppCompatActivity(), RecipeMakananView {

    private lateinit var presenter: DetailPresenter
    private lateinit var favorites: FavoritesPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        Realm.init(this)
        RealmManager.initRealm()

        val btnBack = findViewById<ImageView>(R.id.btnBack)

        val userDataStoreImpl = UserDataStoreImpl(this)
        presenter = DetailPresenter()
        favorites = FavoritesPresenter(this, userDataStoreImpl)

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
            dataItem?.let {
                favorites.addFavorite(it.id) { success ->
                    runOnUiThread {
                        if (success) {
                            Toast.makeText(this, "Resep berhasil disimpan ke favorit", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Gagal menyimpan resep ke favorit", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
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