package com.alysa.myrecipe

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.alysa.myrecipe.auth.presentation.SignInActivity
import com.alysa.myrecipe.auth.presenter.UserPresenter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.alysa.myrecipe.core.utils.RealmManager
import com.alysa.myrecipe.home.presentation.HomeFragment
import io.realm.Realm

class MainActivity : AppCompatActivity() {
    private lateinit var presenter: UserPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Realm.init(this)
        RealmManager.initRealm()

        presenter = UserPresenter(this)

        if (!presenter.isUserLoggedIn()) {
            // Pengguna belum login, arahkan ke activity login
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        // Initialize views
        val navView: BottomNavigationView = findViewById(R.id.bottomNavigation)

        // Setup NavController
        val navController =
            supportFragmentManager.findFragmentById(R.id.navHost)!!
                .findNavController()

        // Setup AppBarConfiguration
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_food,
                R.id.navigation_drink,
                R.id.navigation_recipe
            )
        )
        // Setup BottomNavigationView with NavController
        navView.setupWithNavController(navController)
    }


    override fun onBackPressed() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHost)
        val currentDestination = navHostFragment?.findNavController()?.currentDestination?.id

        if (currentDestination == R.id.navigation_home) {
            super.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }
}


//    override fun displayUser(result: ResultState<UserModel>) {
//        when (result) {
//            is ResultState.Success -> {
//                val userData = result.data
//                // Handle success case
//            }
//            is ResultState.Error -> {
//                val errorMessage = result.error
//                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
//                // Handle error case
//            }
//            is ResultState.Loading -> {
//                Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show()
//                // Handle loading state
//            }
//        }
//    }
//}
