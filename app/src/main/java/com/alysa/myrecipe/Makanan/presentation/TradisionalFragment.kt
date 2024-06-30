package com.alysa.myrecipe.Makanan.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alysa.myrecipe.Makanan.adapter.ResepMakananAdapter
import com.alysa.myrecipe.Makanan.presenter.RecipeMakananPresenter
import com.alysa.myrecipe.R
import com.alysa.myrecipe.core.domain.recipe.makanan.DataItem
import com.alysa.myrecipe.core.remote.ApiConfig
import com.alysa.myrecipe.core.remote.ApiServiceRecipeMakanan
import com.alysa.myrecipe.core.utils.RealmManager
import com.alysa.myrecipe.core.utils.ResultState
import com.alysa.myrecipe.core.utils.SpacesItemDecoration
import com.alysa.myrecipe.core.view.RecipeMakananView
import com.alysa.myrecipe.recipe.detail.view.DetailActivity
import io.realm.Realm
import io.realm.RealmList

class TradisionalFragment : Fragment(), RecipeMakananView {

    private lateinit var presenter: RecipeMakananPresenter
    private lateinit var adapter: ResepMakananAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = ResepMakananAdapter(requireContext(),  object : ResepMakananAdapter.OnItemClickListener{
            override fun onItemClick(data: DataItem) {
                val productId = data.id ?: ""
                Log.d("Tradisional", "Product ID clicked: $productId")

                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("id", productId)
                startActivity(intent)
            }
        }, showResepButton = false)

        val apiServiceProduct =
            ApiConfig.getApiService(requireContext(), "recipeMakanan") as? ApiServiceRecipeMakanan
        Log.d("ApiServiceProduct", "ApiServiceProduct is not null: $apiServiceProduct")

        apiServiceProduct?.let {
            presenter = RecipeMakananPresenter(it, this)
            getContent()
        } ?: Log.e("TradisionalFragment", "Failed to initialize ApiServiceProduct")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tradisional, container, false)

        var recyclerView = view.findViewById<RecyclerView>(R.id.rv_tradisional)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(SpacesItemDecoration(3))

        recyclerView.adapter = adapter


        setLoading(adapter.itemCount == 0)

        val refresh =  view.findViewById<SwipeRefreshLayout>(R.id.swiperefresh)
        refresh.setOnRefreshListener{
            getContent()
            if (refresh.isRefreshing) {
                refresh.isRefreshing = false
            }
        }

        return view
    }

    private fun getContent() {
        presenter.getRecipeMakanan("2","2")
    }

    private fun setLoading(isLoading: Boolean) {
        val viewLoading = view?.findViewById<RelativeLayout>(R.id.view_loading)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.rv_asia)

        if (isLoading) {
            viewLoading?.visibility = View.VISIBLE
            recyclerView?.visibility = View.GONE
        } else {
            viewLoading?.visibility = View.INVISIBLE
            recyclerView?.visibility = View.VISIBLE
        }
    }

    override fun displayRecipe(result: ResultState<List<DataItem>?>) {
        when (result) {
            is ResultState.Success -> {
                val productData = result.data
                productData?.let {
                    adapter.updateData(it)
                    setLoading(it.isEmpty())
                } ?: run {
                    setLoading(true)
                    Toast.makeText(requireContext(), "No data available", Toast.LENGTH_SHORT).show()
                }
            }
            is ResultState.Error -> {
                // Handle jika terjadi error
                val errorMessage = result.error
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
            is ResultState.Loading -> {
                // Handle loading state
                setLoading(true)
                Toast.makeText(requireContext(), "Loading..", Toast.LENGTH_SHORT).show()
            }
        }
    }
}