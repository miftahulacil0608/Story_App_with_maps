package com.example.storyapp.ui


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.data.SharedPreferences
import com.example.storyapp.data.remote.ApiConfig
import com.example.storyapp.databinding.ActivityMainStoryAppBinding
import com.example.storyapp.maps.MapsActivity
import com.example.storyapp.adapter.StoryListAdapter
import com.example.storyapp.repository.StoryListRepository
import com.example.storyapp.ui.viewModel.StoryListViewModel
import com.example.storyapp.paging3dulu.StoryListViewModelFactory
import com.example.storyapp.ui.viewModel.MainActivityStoryAppViewModel


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token_setting")

class MainActivityStoryApp : AppCompatActivity() {
    private lateinit var binding: ActivityMainStoryAppBinding

    private lateinit var mainActivityStoryAppViewModel: MainActivityStoryAppViewModel
    private lateinit var storyListViewModel: StoryListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainStoryAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showRecyclerview()

        setupData()

        binding.btnAddStory.setOnClickListener {
            val intent = Intent(this@MainActivityStoryApp, StoryActivity::class.java)
            startActivity(intent)
            onStart()
        }

    }

    private fun showRecyclerview() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStory.addItemDecoration(itemDecoration)

    }

    private fun setupData() {
        val adapter = StoryListAdapter()
        binding.rvStory.adapter = adapter
        val repository = StoryListRepository(ApiConfig.getApiService(this@MainActivityStoryApp))
        mainActivityStoryAppViewModel = ViewModelProvider(
            this,
            ViewModelFactory(SharedPreferences.instance(dataStore))
        )[MainActivityStoryAppViewModel::class.java]

        storyListViewModel = ViewModelProvider(
            this,
            StoryListViewModelFactory(repository)
        )[StoryListViewModel::class.java]

        setupAction(adapter)

    }

    private fun setupAction(adapter: StoryListAdapter) {
        mainActivityStoryAppViewModel.apply {
            getUser().observe(this@MainActivityStoryApp) {
                if (!it.isLogin) {
                    startActivity(Intent(this@MainActivityStoryApp, WelcomeActivity::class.java))
                    finish()
                }
            }
            storyListViewModel.story.observe(this@MainActivityStoryApp) {
                adapter.submitData(lifecycle, it)
            }

        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                mainActivityStoryAppViewModel.logout()
                startActivity(Intent(this@MainActivityStoryApp, LoginActivity::class.java))
                finish()
                return true
            }
            R.id.menu_upload -> {
                startActivity(Intent(this@MainActivityStoryApp, StoryActivity::class.java))
                return true
            }
            R.id.menu_setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                return true
            }
            R.id.menu_maps -> {
                startActivity(Intent(this@MainActivityStoryApp, MapsActivity::class.java))
                return true
            }
            else -> return true
        }
    }
}