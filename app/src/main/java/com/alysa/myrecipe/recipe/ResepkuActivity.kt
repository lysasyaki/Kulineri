package com.alysa.myrecipe.recipe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alysa.myrecipe.R
import com.alysa.myrecipe.auth.presenter.ProfilePresenter
import com.alysa.myrecipe.core.domain.Favorite.get.DataGet
import com.alysa.myrecipe.core.domain.recipe.byUser.DataByUser
import com.alysa.myrecipe.core.domain.recipe.delete.DataDelete
import com.alysa.myrecipe.core.remote.ApiConfig
import com.alysa.myrecipe.core.remote.ApiServiceGetFavorite
import com.alysa.myrecipe.core.remote.ApiServiceRecipeByUser
import com.alysa.myrecipe.core.utils.ResultState
import com.alysa.myrecipe.core.utils.SpacesItemDecoration
import com.alysa.myrecipe.core.utils.UserDataStoreImpl
import com.alysa.myrecipe.core.view.RecipeByUser
import com.alysa.myrecipe.core.view.RecipeFavorite
import com.alysa.myrecipe.recipe.detail.presenter.GetFavPresenter
import com.alysa.myrecipe.recipe.detail.view.DetailActivity

class ResepkuActivity : AppCompatActivity() , RecipeByUser {

    private lateinit var presenter: ProfilePresenter
    private lateinit var adapter: ResepkuAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resepku)

        val userDataStoreImpl = UserDataStoreImpl(this)

        adapter = ResepkuAdapter(this, object : ResepkuAdapter.OnItemClickListener {
            override fun onItemClick(data: DataByUser) {
                val productId = data.id ?: 0
                Log.d("ResepkuActivity", "Product ID clicked: $productId")

                val intent = Intent(this@ResepkuActivity, DetailActivity::class.java)
                intent.putExtra("id", productId)
                startActivity(intent)
            }
        }, object : ResepkuAdapter.OnDeleteClickListener {
            override fun onDeleteClick(data: DataByUser) {
                deleteRecipe(data.id ?: 0)
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

        val apiServiceRecipeByUser = ApiConfig.getApiService(this, "getRecipe") as? ApiServiceRecipeByUser
        apiServiceRecipeByUser?.let {
            presenter = ProfilePresenter(this, this, userDataStoreImpl)
            getContent()
        } ?: Log.e("Resepku", "Failed to initialize ApiServiceRecipeByUser")
    }

    private fun getContent() {
        presenter.getRecipeByUser()
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

    override fun displayRecipe(result: ResultState<List<DataByUser>?>) {
        when (result) {
            is ResultState.Success -> {
                setLoading(false)
                val favoriteDataList = result.data
                favoriteDataList?.forEach { data ->
                    Log.d("ResepkuActivity", "Recipe Data: ${data.name}, ${data.description}, ${data.image}")
                }
                adapter.updateData(favoriteDataList ?: emptyList())
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

//    private fun showDeleteConfirmationDialog(recipeId: Int) {
//        val builder = AlertDialog.Builder(this)
//        builder.setTitle("Delete Recipe")
//            .setMessage("Are you sure you want to delete this recipe?")
//            .setPositiveButton("Delete") { dialog, which ->
//                // Call deleteRecipe function
//                deleteRecipe(recipeId)
//            }
//            .setNegativeButton("Cancel") { dialog, which ->
//                dialog.dismiss()
//            }
//            .show()
//    }

    private fun deleteRecipe(id: Int) {
        presenter.deleteRecipe(id) { success ->
            if (success) {
                // Handle success, if needed
                Toast.makeText(this, "Recipe deleted successfully", Toast.LENGTH_SHORT).show()
                getContent()
            } else {
                // Handle failure, if needed
                Toast.makeText(this, "Failed to delete recipe", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onBackClicked(view: View) {
        onBackPressed()
    }
}
