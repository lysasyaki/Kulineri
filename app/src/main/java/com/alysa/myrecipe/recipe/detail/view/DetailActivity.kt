package com.alysa.myrecipe.recipe.detail.view

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.alysa.myrecipe.R
import com.alysa.myrecipe.auth.model.UserPreferences
import com.alysa.myrecipe.core.domain.recipe.detail.DataDetail
import com.alysa.myrecipe.core.utils.ResultState
import com.alysa.myrecipe.core.utils.UserDataStoreImpl
import com.alysa.myrecipe.core.view.RecipeDetailView
import com.alysa.myrecipe.recipe.detail.presenter.DetailPresenter
import com.alysa.myrecipe.recipe.detail.presenter.FavoritesPresenter
import com.bumptech.glide.Glide
import com.google.gson.Gson

class DetailActivity : AppCompatActivity(), RecipeDetailView {

    private lateinit var presenter: DetailPresenter
    private lateinit var favorites: FavoritesPresenter
    private lateinit var btnFavorite: ImageView
    private var isFavorite: Boolean = false
    private var recipeId: Int = 0
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        btnFavorite = findViewById(R.id.btnFavorite)

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val userPreferencesJson = sharedPreferences.getString("user_preferences", null)
        if (userPreferencesJson != null) {
            userPreferences = Gson().fromJson(userPreferencesJson, UserPreferences::class.java)
        } else {

            userPreferences = UserPreferences()
        }
        favorites = FavoritesPresenter(this, userPreferences)

        val userDataStoreImpl = UserDataStoreImpl(this)
        presenter = DetailPresenter(this, userDataStoreImpl, this)

        val uniqueId = intent.getIntExtra("id", 0)
        recipeId = uniqueId
        presenter.getDetailRecipe(uniqueId)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            onBackPressed()
        }

        updateFavoriteButton()

        btnFavorite.setOnClickListener {
            toggleFavorite()
        }
    }

    private fun toggleFavorite() {
        if (isFavorite) {
            favorites.deleteFavorite(recipeId, userPreferences) { success ->
                if (success) {
                    isFavorite = false
                    updateFavoriteButton()
                    showToast("Removed from favorites")
                } else {
                    showToast("Failed to remove from favorites")
                }
            }
        } else {
            favorites.addFavorite(recipeId, userPreferences) { success ->
                if (success) {
                    isFavorite = true
                    updateFavoriteButton()
                    showToast("Added to favorites")
                } else {
                    showToast("Failed to add to favorites")
                }
            }
        }
    }

    private fun updateFavoriteButton() {
        if (isFavorite) {
            btnFavorite.setImageResource(R.drawable.icon_favorite_red)
        } else {
            btnFavorite.setImageResource(R.drawable.icon_favorite_white)
        }
    }

    override fun displayDetail(result: ResultState<DataDetail>) {
        when (result) {
            is ResultState.Success -> {
                val dataDetail = result.data
                if (dataDetail != null) {
                    isFavorite = userPreferences.favoriteRecipes.contains(recipeId)
                    updateFavoriteButton()

                    val tvRecipe = findViewById<TextView>(R.id.tvRecipeName)
                    tvRecipe.text = dataDetail.name?.toUpperCase() ?: ""

                    val tvName = findViewById<TextView>(R.id.tvName)
                    tvName.text = dataDetail.user?.name ?: ""

                    val tvDesc = findViewById<TextView>(R.id.tvDesc)
                    tvDesc.text = dataDetail.description ?: ""

                    val tvBahan = findViewById<TextView>(R.id.tvBahan)
                    tvBahan.text = dataDetail.ingredient ?: ""

                    val tvLangkah = findViewById<TextView>(R.id.tvLangkah)
                    tvLangkah.text = dataDetail.instruction ?: ""

                    val imgResep = findViewById<ImageView>(R.id.ivRecipe)
                    val imageUrl = dataDetail.image?.getOrNull(0)

                    Glide.with(this)
                        .load(imageUrl)
                        .placeholder(R.drawable.gambar_default)
                        .error(R.drawable.gambar_default)
                        .centerCrop()
                        .into(imgResep)

                    presenter.currentDataItem = dataDetail
                } else {
                    Toast.makeText(this, "Data not found", Toast.LENGTH_SHORT).show()
                }
            }
            is ResultState.Error -> {
                val errorMessage = result.error
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
            is ResultState.Loading -> {
                Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
