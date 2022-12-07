package com.example.storyappsubmission1.Activity.Main

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.storyappsubmission1.Activity.Start.LoginActivity
import com.example.storyappsubmission1.Activity.Start.WelcomeActivity
import com.example.storyappsubmission1.R
import com.example.storyappsubmission1.ViewModel.MainVM
import com.example.storyappsubmission1.databinding.ActivityMainBinding
import com.example.storyappsubmission1.Data.Adapter.StoryAdapter
import com.example.storyappsubmission1.Data.Functon.Preference
import com.example.storyappsubmission1.Data.Response.ListStoryR
import com.example.storyappsubmission1.ViewModel.FactoryVM

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainVM
    private lateinit var binding: ActivityMainBinding
    private lateinit var arrayListStoryItem: ArrayList<ListStoryR>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupViewModel()
        binding.post.setOnClickListener {
            val intent = Intent(this@MainActivity, StoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.top_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                mainViewModel.deleteUser()
                val i = Intent(this, LoginActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
                return true
            }
            else -> return true
        }
    }

    override fun onRestart() {
        super.onRestart()
        mainViewModel.listStoryItems.observe(this) { listStoriesItems ->
            showRecyclerList(listStoriesItems)
        }
    }

    private fun setupView() {
        window.insetsController?.hide(WindowInsets.Type.statusBars())
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this, FactoryVM(Preference.getInstance(dataStore))
        )[MainVM::class.java]

        mainViewModel.getUser().observe(this) { user ->
            if (user.token.isEmpty()) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                mainViewModel.getStories(user.token)
            }
        }

        mainViewModel.listStoryItems.observe(this) { listStoriesItems ->
            showRecyclerList(listStoriesItems)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showRecyclerList(listStories: List<ListStoryR>) {
        binding.apply {
            rvStory.setHasFixedSize(true)

            if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
                rvStory.layoutManager = GridLayoutManager(this@MainActivity, 2)
            else
                rvStory.layoutManager = GridLayoutManager(this@MainActivity, 1)

            arrayListStoryItem = ArrayList()
            arrayListStoryItem.addAll(listStories)
            val listStoriesAdapter = StoryAdapter(arrayListStoryItem)
            rvStory.adapter = listStoriesAdapter
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.apply {
            visibility = if (isLoading) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }
}