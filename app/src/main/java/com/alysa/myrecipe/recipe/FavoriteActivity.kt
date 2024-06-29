package com.alysa.myrecipe.recipe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alysa.myrecipe.R
import com.alysa.myrecipe.core.domain.Favorite.get.DataGet
import com.alysa.myrecipe.core.remote.ApiConfig
import com.alysa.myrecipe.core.remote.ApiServiceGetFavorite
import com.alysa.myrecipe.core.utils.ResultState
import com.alysa.myrecipe.core.utils.SpacesItemDecoration
import com.alysa.myrecipe.core.view.RecipeFavorite
import com.alysa.myrecipe.recipe.detail.presenter.GetFavPresenter
import com.alysa.myrecipe.recipe.detail.view.DetailActivity

class FavoriteActivity : AppCompatActivity(), RecipeFavorite {

    private lateinit var presenter: GetFavPresenter
    private lateinit var adapter: FavoriteAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        adapter = FavoriteAdapter(this, object : FavoriteAdapter.OnItemClickListener {
            override fun onItemClick(data: DataGet) {
                val productId = data.recipeId ?: 0
                Log.d("MainActivity", "Product ID clicked: $productId")

                val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                intent.putExtra("id", productId)
                startActivity(intent)
            }
        })

        val recyclerView = findViewById<RecyclerView>(R.id.rv_fav)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this) // Tambahkan ini
        recyclerView.addItemDecoration(SpacesItemDecoration(6))
        swipeRefreshLayout = findViewById(R.id.swiperefresh)
        swipeRefreshLayout.setOnRefreshListener {
            getContent()
        }

        val apiServiceGetFavorite = ApiConfig.getApiService(this, "getFavorite") as? ApiServiceGetFavorite
        apiServiceGetFavorite?.let {
            presenter = GetFavPresenter(this, this, apiServiceGetFavorite)
            getContent()
        } ?: Log.e("Favorite", "Failed to initialize ApiServiceGetFavorite")
    }

    private fun getContent() {
        presenter.getFavorite(this)
    }

    private fun setLoading(isLoading: Boolean) {
        val viewLoading = findViewById<RelativeLayout>(R.id.view_loading)
        val recyclerView = findViewById<RecyclerView>(R.id.rv_fav)

        if (isLoading) {
            viewLoading.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            viewLoading.visibility = View.INVISIBLE
            recyclerView.visibility = View.VISIBLE
        }

        if (swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun displayFavorite(result: ResultState<List<DataGet>?>) {
        when (result) {
            is ResultState.Success -> {
                setLoading(false)
                val favoriteDataList = result.data
                favoriteDataList?.let { adapter.updateData(it) }
            }
            is ResultState.Error -> {
                setLoading(false)
                Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
            }
            is ResultState.Loading -> {
                setLoading(true)
            }
        }
    }

    fun onBackClicked(view: View) {
        onBackPressed()
    }
}
