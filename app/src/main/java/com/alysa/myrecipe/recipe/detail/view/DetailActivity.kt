package com.alysa.myrecipe.recipe.detail.view

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
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
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import com.google.gson.Gson

class DetailActivity : AppCompatActivity(), RecipeDetailView {

    private lateinit var presenter: DetailPresenter
    private lateinit var favorites: FavoritesPresenter
    private lateinit var btnFavorite: ImageView
    private var isFavorite: Boolean = false
    private var recipeId: Int = 0
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userPreferences: UserPreferences
    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var audioManager: AudioManager
//    private lateinit var volumeSeekBar: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        btnFavorite = findViewById(R.id.btnFavorite)
        playerView = findViewById(R.id.video)
//        volumeSeekBar = findViewById(R.id.volume_seekbar)

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

        // Inisiasi ExoPlayer
        player = ExoPlayer.Builder(this).build()
        playerView.player = player

        // Inisiasi AudioManager
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        // Atur kontrol volume untuk menggunakan tombol volume perangkat
        volumeControlStream = AudioManager.STREAM_MUSIC

//
//        // Atur volume player berdasarkan seek bar
//        volumeSeekBar.progress = (player.volume * 100).toInt()
//        volumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                player.volume = progress / 100f
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
//            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
//        })
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

                    // Load video jika tersedia
                    val videoUrl = dataDetail.video?.getOrNull(0)
                    if (videoUrl != null) {
                        val mediaItem = MediaItem.fromUri(videoUrl)
                        player.setMediaItem(mediaItem)
                        player.prepare()
                        player.play()
                    }

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

    override fun onDestroy() {
        super.onDestroy()
        // Pastikan untuk melepaskan player saat aktivitas dihancurkan
        player.release()
    }
}
