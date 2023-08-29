package com.example.portal

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.room.Room
import com.example.portal.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var db: AppDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window

            // Clear FLAG_TRANSLUCENT_STATUS flag
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

            // Add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

            // Finally, change the status bar color
            window.statusBarColor = ContextCompat.getColor(this, R.color.maroon)
        }

        // ... Rest of the code ...

        // Set text for navigation header TextViews



        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val bottomNavView: BottomNavigationView = binding.bottomNav
        val navController = findNavController(R.id.nav_host_fragment_content_main)





        db = Room.databaseBuilder(
            this, // Use 'this' to refer to the context of the Activity
            AppDatabase::class.java, "user-database"
        )
            .fallbackToDestructiveMigration()
            .build()


        sessionManager = SessionManager(this)





       // Initialize sessionManager

        // Load the appropriate navigation graph based on login status
        val navGraphId = if (sessionManager.isLoggedIn) {
            R.navigation.nav2
        } else {
            R.navigation.mobile_navigation
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.FirstFragment || destination.id == R.id.SignupFragment) {
                binding.bottomNav.visibility = View.GONE
            } else {
                binding.bottomNav.visibility = View.VISIBLE
            }
        }

        navController.graph = navController.navInflater.inflate(navGraphId)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        bottomNavView.setupWithNavController(navController)

        // Set up navigation drawer item click listener
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {
                    // Show a confirmation dialog before logging out
                    showLogoutConfirmationDialog()
                    sessionManager.logout2(this@MainActivity)
                    drawerLayout.closeDrawers()
                    true
                }
                // Handle other menu items here
                else -> {
                    // For other menu items, perform navigation as usual
                    drawerLayout.closeDrawers() // Close the drawer
                    menuItem.onNavDestinationSelected(navController) // Navigate based on the item ID
                    return@setNavigationItemSelectedListener true // Explicitly return true for other cases
                }
            }
        }


        bottomNavView.apply {
            setOnNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.nav_logout -> {
                        // Show a confirmation dialog before logging out
                        showLogoutConfirmationDialog()
                        sessionManager.logout2(this@MainActivity)
                        true
                    }
                    // Handle other menu items here
                    else -> {
                        // For other menu items, perform navigation as usual
                        menuItem.onNavDestinationSelected(navController) // Navigate based on the item ID
                        true
                    }
                }
            }
        }





    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        if (navController.currentDestination?.id != R.id.FirstFragment) {
            // Show the back button in the app bar
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } else {
            // Hide the back button in the app bar
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        Log.d("Navigation", "Current Destination: ${navController.currentDestination?.id}")
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to log out?")
        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            sessionManager.logout()
            findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.FirstFragment)
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}
