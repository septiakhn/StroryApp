package com.example.stroryapp.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stroryapp.R
import com.example.stroryapp.data.UserAdapter
import com.example.stroryapp.databinding.ActivityMainBinding
import com.example.stroryapp.response.ListStoryItem
import com.example.stroryapp.viewModel.MainViewModel
import com.example.stroryapp.viewModel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                viewModel.getStory(user.token).observe(this) { story ->
                    if (story != null) {
                        binding.progressBar.visibility = View.GONE
                        val adapter = UserAdapter()
                        binding.rvReview.layoutManager = LinearLayoutManager(this)
                        adapter.submitData(lifecycle, story)
                        binding.rvReview.adapter = adapter
                        binding.halo.text = resources.getString(R.string.greeting)
                        Log.d("nama penggunan ", "onCreate: ${user.id}")
                    }
                }
            }
        }
        binding.menu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.logout -> {
                    viewModel.getLogout()
                    finish()
                    true
                }else -> false

//                R.id.map -> {
//                    val intent = Intent(this@MainActivity, MapsActivity::class.java)
//                    startActivity(intent)
//                    true
//                }

//                else -> false
            }
        }
    }
}
