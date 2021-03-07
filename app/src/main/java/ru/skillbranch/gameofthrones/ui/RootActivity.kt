package ru.skillbranch.gameofthrones.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavAction
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_root.*
import ru.skillbranch.gameofthrones.R

class RootActivity : AppCompatActivity() {
    private lateinit var rootViewModel : RootViewModel
    lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
        initViewModel()
        savedInstanceState ?: prepareData()
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
    }

    private fun prepareData() : Unit {
        rootViewModel.syncDataIfNeed().observe(this, Observer<LoadResult<Boolean>> {
            when (it) {
                is LoadResult.Loading -> {
                    navController.navigate(R.id.nav_splash)
                }
                is LoadResult.Success -> {
                    //var action : NavDirections = SplashFragmentDirections.actionNavSplashToNavHouses()
                    navController.navigate(R.id.action_nav_splash_to_nav_houses)
                }
                is LoadResult.Error -> {
                    Snackbar.make(
                        root_container,
                        it.errorMessage.toString(),
                        Snackbar.LENGTH_INDEFINITE
                    ).show()
                }
            }
        })
    }

    private fun initViewModel() {
        rootViewModel = ViewModelProviders.of(this).get(RootViewModel::class.java)
    }
}