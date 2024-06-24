package com.alysa.myrecipe.home.presentation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.alysa.myrecipe.R
import com.alysa.myrecipe.core.domain.recipe.Data
import com.alysa.myrecipe.core.remote.ApiConfig
import com.alysa.myrecipe.core.remote.ApiServiceRecipeType
import com.alysa.myrecipe.core.utils.RealmManager
import com.alysa.myrecipe.core.utils.ResultState
import com.alysa.myrecipe.core.utils.SpacesItemDecoration
import com.alysa.myrecipe.core.view.RecipeTypeView
import com.alysa.myrecipe.home.adapter.CarouselAdapter
import com.alysa.myrecipe.home.adapter.MakananAdapter
import com.alysa.myrecipe.home.adapter.MinumanAdapter
import com.alysa.myrecipe.home.presenter.RecipeTypePresenter
import com.alysa.myrecipe.home.presenter.slider
import com.alysa.myrecipe.recipe.detail.view.DetailActivity
import io.realm.Realm

class HomeFragment : Fragment(), RecipeTypeView {

    private lateinit var presenterRecipe: RecipeTypePresenter

    private lateinit var indicatorsContainer: LinearLayout
    private lateinit var makananAdapter: MakananAdapter
    private lateinit var minumanAdapter: MinumanAdapter

    private lateinit var rvMakanan: RecyclerView
    private lateinit var rvMinuman: RecyclerView

    private val carouselAdapter = CarouselAdapter(
        listOf(
            slider(R.drawable.carousel1),
            slider(R.drawable.carousel2),
            slider(R.drawable.carousel3)
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Realm.init(requireContext())
        RealmManager.initRealm()

        // Inisialisasi adapter dan set ke RecyclerView
        makananAdapter = MakananAdapter(requireContext(), object : MakananAdapter.OnItemClickListener {
            override fun onItemClick(data: Data) {
                val productId = data. id ?: ""
                Log.d("MainActivity", "Product ID clicked: $productId")

                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("id", productId)
                startActivity(intent)
            }
        })

        minumanAdapter = MinumanAdapter(requireContext(), object : MinumanAdapter.OnItemClickListener {
            override fun onItemClick(data: Data) {
                val productId = data.id ?: ""
                Log.d("MainActivity", "Product ID clicked: $productId")

                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("id", productId)
                startActivity(intent)
            }
        })

        val apiRecipeByTypeId =
            ApiConfig.getApiService(requireContext(), "recipeType") as? ApiServiceRecipeType
        Log.d("ApiRecipeByType", "ApiRecipeByType is not null: $apiRecipeByTypeId")

        if (apiRecipeByTypeId != null) {
            presenterRecipe = RecipeTypePresenter(apiRecipeByTypeId, this)
        } else {
            Log.e("Dashboard", "Failed to initialize ApiRecipeByType")
            Toast.makeText(
                requireContext(),
                "Failed to initialize ApiRecipeByType",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        rvMakanan = view.findViewById(R.id.rv_food)
        rvMinuman = view.findViewById(R.id.rv_drink)

        val viewPager = view.findViewById<ViewPager2>(R.id.vp_Carousel)
        indicatorsContainer = view.findViewById(R.id.inContainer)

        // Set adapter to ViewPager
        viewPager.adapter = carouselAdapter

        // Set up indicators
        setupIndicators(indicatorsContainer)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })

        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                val currentPosition = viewPager.currentItem
                val newPosition = if (currentPosition == carouselAdapter.itemCount - 1) 0 else currentPosition + 1
                viewPager.setCurrentItem(newPosition, true)
                handler.postDelayed(this, 5000) // 5000 milliseconds = 5 seconds
            }
        }
        handler.postDelayed(runnable, 5000)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == carouselAdapter.itemCount - 1) {
                    // Jika sampai ke page terakhir, kembali ke page pertama setelah sedikit jeda
                    Handler().postDelayed({
                        viewPager.setCurrentItem(0, false) // Set animasi menjadi false
                    }, 2000) // Ganti 2000 dengan jeda yang diinginkan sebelum kembali ke page pertama (dalam milidetik)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    // Jika pengguna mencoba untuk menggeser halaman dari terakhir, langsung kembali ke halaman pertama
                    val currentItem = viewPager.currentItem
                    val lastItem = carouselAdapter.itemCount - 1
                    if (currentItem == lastItem) {
                        viewPager.setCurrentItem(0, false)
                    }
                }
            }
        })

        initRecyclerView(makananAdapter, rvMakanan)
        initRecyclerView(minumanAdapter, rvMinuman)

        getContent()

        val refresh =  view.findViewById<SwipeRefreshLayout>(R.id.swiperefresh)
        refresh.setOnRefreshListener{
            getContent()
            if (refresh.isRefreshing) {
                refresh.isRefreshing = false
            }
        }

        return view
    }

    private fun setupIndicators(indicatorsContainer: LinearLayout) {
        val indicators = arrayOfNulls<ImageView>(carouselAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        layoutParams.setMargins(8, 0, 8, 0)

        for (i in indicators.indices) {
            indicators[i] = ImageView(requireContext())
            indicators[i]?.apply {
                setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.unselected_dot
                    )
                )
                this.layoutParams = layoutParams
            }
            indicatorsContainer.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index: Int) {
        val childCount = indicatorsContainer.childCount
        for (i in 0 until childCount) {
            val imageView = indicatorsContainer[i] as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.selected_dot
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.unselected_dot
                    )
                )
            }
        }
    }

    private fun getContent() {
        minumanAdapter.clearData()
        makananAdapter.clearData()
        presenterRecipe.getRecipeByType("1")
        presenterRecipe.getRecipeByType("2")

    }

    private fun initRecyclerView(adapter: RecyclerView.Adapter<*>, recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(SpacesItemDecoration(6))
    }

    private fun setLoading(isLoading: Boolean, recyclerViewType: String) {
        val viewLoading: RelativeLayout?
        val recyclerView: RecyclerView?

        val makanan = view?.findViewById<TextView>(R.id.tv_recommendation_food)
        val minuman = view?.findViewById<TextView>(R.id.tv_recommendation_drink)

        // Menyembunyikan TextView saat isLoading true
        makanan?.visibility = if (isLoading) View.GONE else View.VISIBLE
        minuman?.visibility = if (isLoading) View.GONE else View.VISIBLE

        when (recyclerViewType) {
            "makanan" -> {
                viewLoading = view?.findViewById(R.id.view_loading_food)
                recyclerView = rvMakanan
            }
            "minuman" -> {
                viewLoading = view?.findViewById(R.id.view_loading_drink)
                recyclerView = rvMinuman
            }
            else -> {
                viewLoading = null
                recyclerView = null
            }
        }

        viewLoading?.visibility = if (isLoading) View.VISIBLE else View.GONE
        recyclerView?.visibility = if (isLoading) View.GONE else View.VISIBLE
    }



    override fun displayRecipe(result: ResultState<List<Data>?>) {
        when (result) {
            is ResultState.Success -> {
                // Handle ketika data berhasil diterima
                val resepData = result.data
                for (resep in resepData.orEmpty()) { // orEmpty() untuk menghindari NPE jika result.data null
                    when (resep.unitId) {
                        2 -> {
                            makananAdapter.addData(resep)
                            setLoading(false, "makanan")
                        }
                        1 -> {
                            minumanAdapter.addData(resep)
                            setLoading(false, "minuman")
                        }
                        else -> {
                            Log.e("HomeFragment", "Unexpected typeId: ${resep.unitId}")
                        }
                    }
                }
            }
            is ResultState.Error -> {
                // Handle ketika terjadi error
                val errorMessage = result.error ?: "Unknown error"
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                setLoading(false, "") // Set isLoading false untuk semua RecyclerView
            }
            is ResultState.Loading -> {
                // Handle ketika sedang dalam proses loading
                Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_SHORT).show()
                setLoading(true, "") // Set isLoading true untuk semua RecyclerView
            }
        }
    }
}


//package com.alysa.myrecipe.home.presentation
//
//import android.os.Bundle
//import android.os.Handler
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.AdapterView
//import android.widget.ImageView
//import android.widget.LinearLayout
//import android.widget.RelativeLayout
//import android.widget.TextView
//import android.widget.Toast
//import androidx.core.content.ContextCompat
//import androidx.core.view.get
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
//import androidx.viewpager2.widget.ViewPager2
//import com.alysa.myrecipe.R
//import com.alysa.myrecipe.core.domain.recipe.Data
//import com.alysa.myrecipe.core.remote.ApiConfig
//import com.alysa.myrecipe.core.remote.ApiServiceRecipeType
//import com.alysa.myrecipe.core.utils.ResultState
//import com.alysa.myrecipe.core.utils.SpacesItemDecoration
//import com.alysa.myrecipe.core.view.RecipeTypeView
//import com.alysa.myrecipe.home.adapter.CarouselAdapter
//import com.alysa.myrecipe.home.adapter.MakananAdapter
//import com.alysa.myrecipe.home.adapter.MinumanAdapter
//import com.alysa.myrecipe.home.presenter.RecipeTypePresenter
//import com.alysa.myrecipe.home.presenter.slider
//
//class HomeFragment : Fragment(), RecipeTypeView {
//
//    private lateinit var presenterRecipe: RecipeTypePresenter
//
//    private lateinit var indicatorsContainer: LinearLayout
//    private lateinit var makananAdapter: MakananAdapter
//    private lateinit var minumanAdapter: MinumanAdapter
//
//    private lateinit var rvMakanan: RecyclerView
//    private lateinit var rvMinuman: RecyclerView
//
//    private val carouselAdapter = CarouselAdapter(
//        listOf(
//            slider(R.drawable.carousel1),
//            slider(R.drawable.carousel2),
//            slider(R.drawable.carousel3)
//        )
//    )
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // Initialize adapters
//        makananAdapter = MakananAdapter(requireContext(), AdapterView.OnItemClickListener { _, _, position, _ ->
//            val productId = makananAdapter.getData()?.get(position)?.id ?: ""
//            Log.d("MainActivity", "Product ID clicked: $productId")
//            // Uncomment to start DetailActivity
//            // val intent = Intent(requireContext(), DetailActivity::class.java)
//            // intent.putExtra("id", productId)
//            // startActivity(intent)
//        })
//
//        minumanAdapter = MinumanAdapter(requireContext(), AdapterView.OnItemClickListener { _, _, position, _ ->
//            val productId = minumanAdapter.getData()?.get(position)?.id ?: ""
//                Log.d("MainActivity", "Product ID clicked: $productId")
//                // Uncomment to start DetailActivity
//                // val intent = Intent(requireContext(), DetailActivity::class.java)
//                // intent.putExtra("id", productId)
//                // startActivity(intent)
//        })
//
//        val apiRecipeByTypeId =
//            ApiConfig.getApiService(requireContext(), "recipeType") as? ApiServiceRecipeType
//        Log.d("ApiRecipeByType", "ApiRecipeByType is not null: $apiRecipeByTypeId")
//
//        if (apiRecipeByTypeId != null) {
//            presenterRecipe = RecipeTypePresenter(apiRecipeByTypeId, this)
//        } else {
//            Log.e("Dashboard", "Failed to initialize ApiRecipeByType")
//            Toast.makeText(
//                requireContext(),
//                "Failed to initialize ApiRecipeByType",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        val view = inflater.inflate(R.layout.fragment_home, container, false)
//
//        rvMakanan = view.findViewById(R.id.rv_food)
//        rvMinuman = view.findViewById(R.id.rv_drink)
//
//        val viewPager = view.findViewById<ViewPager2>(R.id.vp_Carousel)
//        indicatorsContainer = view.findViewById(R.id.inContainer)
//
//        // Set adapter to ViewPager
//        viewPager.adapter = carouselAdapter
//
//        // Set up indicators
//        setupIndicators(indicatorsContainer)
//
//        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                setCurrentIndicator(position)
//            }
//        })
//
//        val handler = Handler()
//        val runnable = object : Runnable {
//            override fun run() {
//                val currentPosition = viewPager.currentItem
//                val newPosition = if (currentPosition == carouselAdapter.itemCount - 1) 0 else currentPosition + 1
//                viewPager.setCurrentItem(newPosition, true)
//                handler.postDelayed(this, 5000) // 5000 milliseconds = 5 seconds
//            }
//        }
//        handler.postDelayed(runnable, 5000)
//
//        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                if (position == carouselAdapter.itemCount - 1) {
//                    // Jika sampai ke page terakhir, kembali ke page pertama setelah sedikit jeda
//                    Handler().postDelayed({
//                        viewPager.setCurrentItem(0, false) // Set animasi menjadi false
//                    }, 2000) // Ganti 2000 dengan jeda yang diinginkan sebelum kembali ke page pertama (dalam milidetik)
//                }
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {
//                super.onPageScrollStateChanged(state)
//                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
//                    // Jika pengguna mencoba untuk menggeser halaman dari terakhir, langsung kembali ke halaman pertama
//                    val currentItem = viewPager.currentItem
//                    val lastItem = carouselAdapter.itemCount - 1
//                    if (currentItem == lastItem) {
//                        viewPager.setCurrentItem(0, false)
//                    }
//                }
//            }
//        })
//
//        initRecyclerView(makananAdapter, rvMakanan)
////        initRecyclerView(minumanAdapter, rvMinuman)
//
//        getContent()
//
//        val refresh =  view.findViewById<SwipeRefreshLayout>(R.id.swiperefresh)
//        refresh.setOnRefreshListener{
//            getContent()
//            if (refresh.isRefreshing) {
//                refresh.isRefreshing = false
//            }
//        }
//
//        return view
//    }
//
//    private fun setupIndicators(indicatorsContainer: LinearLayout) {
//        val indicators = arrayOfNulls<ImageView>(carouselAdapter.itemCount)
//        val layoutParams: LinearLayout.LayoutParams =
//            LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//            )
//        layoutParams.setMargins(8, 0, 8, 0)
//
//        for (i in indicators.indices) {
//            indicators[i] = ImageView(requireContext())
//            indicators[i]?.apply {
//                setImageDrawable(
//                    ContextCompat.getDrawable(
//                        requireContext(),
//                        R.drawable.unselected_dot
//                    )
//                )
//                this.layoutParams = layoutParams
//            }
//            indicatorsContainer.addView(indicators[i])
//        }
//    }
//
//    private fun setCurrentIndicator(index: Int) {
//        val childCount = indicatorsContainer.childCount
//        for (i in 0 until childCount) {
//            val imageView = indicatorsContainer[i] as ImageView
//            if (i == index) {
//                imageView.setImageDrawable(
//                    ContextCompat.getDrawable(
//                        requireContext(),
//                        R.drawable.selected_dot
//                    )
//                )
//            } else {
//                imageView.setImageDrawable(
//                    ContextCompat.getDrawable(
//                        requireContext(),
//                        R.drawable.unselected_dot
//                    )
//                )
//            }
//        }
//    }
//
//    private fun getContent() {
//        presenterRecipe.getRecipeByType("1")
//        presenterRecipe.getRecipeByType("2")
//    }
//
//    private fun initRecyclerView(adapter: RecyclerView.Adapter<*>, recyclerView: RecyclerView) {
//        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        recyclerView.adapter = adapter
//        recyclerView.addItemDecoration(SpacesItemDecoration(6))
//    }
//
//    private fun setLoading(isLoading: Boolean, recyclerViewType: String) {
//        val viewLoading: RelativeLayout?
//        val recyclerView: RecyclerView?
//
//        val makanan = view?.findViewById<TextView>(R.id.tv_recommendation_food)
//        val minuman = view?.findViewById<TextView>(R.id.tv_recommendation_drink)
//
//        // Menyembunyikan TextView saat isLoading true
//        makanan?.visibility = if (isLoading) View.GONE else View.VISIBLE
//        minuman?.visibility = if (isLoading) View.GONE else View.VISIBLE
//
//        when (recyclerViewType) {
//            "makanan" -> {
//                viewLoading = view?.findViewById(R.id.view_loading_food)
//                recyclerView = rvMakanan
//            }
//            "minuman" -> {
//                viewLoading = view?.findViewById(R.id.view_loading_drink)
//                recyclerView = rvMinuman
//            }
//            else -> {
//                viewLoading = null
//                recyclerView = null
//            }
//        }
//
//        viewLoading?.visibility = if (isLoading) View.VISIBLE else View.GONE
//        recyclerView?.visibility = if (isLoading) View.GONE else View.VISIBLE
//    }
//
//    override fun displayRecipe(result: ResultState<List<Data>?>) {
//        when (result) {
//            is ResultState.Success -> {
//                // Handle data berhasil diterima
//                val resepData = result.data
//                if (resepData != null) {
//                    for (resep in resepData) {
//                        when (resep.unitId) {
//                            2 -> { // Ganti dengan nilai integer 1 karena typeId seharusnya Int, bukan String
//                                makananAdapter.setData(listOf(resep)) // Memanggil setData daripada addData
//                                setLoading(false, "makanan")
//                            }
//                            1 -> { // Ganti dengan nilai integer 1 karena typeId seharusnya Int, bukan String
//                                minumanAdapter.setData(listOf(resep)) // Memanggil setData daripada addData
//                                setLoading(false, "minuman")
//                            }
//                            // Tambahkan kasus lain sesuai kebutuhan
//                            else -> {
//                                Log.e("HomeFragment", "typeId yang tidak terduga: ${resep.unitId}")
//                            }
//                        }
//                    }
//                } else {
//                    Log.e("HomeFragment", "Data hasil null")
//                }
//            }
//
//            is ResultState.Error -> {
//                // Handle jika terjadi error
//                val errorMessage = result.error
//                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
//                setLoading(false, "") // Atur isLoading menjadi false untuk semua RecyclerView
//            }
//            is ResultState.Loading -> {
//                // Handle loading state
//                Toast.makeText(requireContext(), "Loading..", Toast.LENGTH_SHORT).show()
//                setLoading(true, "") // Atur isLoading menjadi true untuk semua RecyclerView
//            }
//        }
//    }
//}