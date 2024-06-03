package com.example.stroryapp.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                Log.d("MainActivity", "User is not logged in, starting WelcomeActivity")
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Log.d("MainActivity", "User is logged in")
            }
        }

        setupView()
        setupAction()
        setupObserver()
        viewModel.getStory()
//        viewModel.loadData()
//        viewModel.data.observe(this) { data ->
//            // Update UI with the loaded data
//            binding.rvReview.adapter = UserAdapter(data)
//        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.btnAdd.setOnClickListener {
            // Action when the FAB is clicked
            // You can replace this with the desired action
            Log.d("MainActivity", "FAB clicked")
        }

        binding.rvReview.layoutManager = LinearLayoutManager(this)
        // Set adapter for RecyclerView
        // binding.rvReview.adapter = yourAdapter

        // Example logout button action
//        binding.logoutButton.setOnClickListener {
//            viewModel.logout()
//        }
    }
    private fun setupObserver() {
        viewModel.storyData.observe(this) { userList ->
            if (userList != null) {
                // Convert List<UserModel> to List<ListStoryItem>
                val storyItemList = userList.map { user ->
                    ListStoryItem(
                        id = user.id
                        // Add any other necessary conversions
                    )
                }
                // Pass storyItemList to the UserAdapter constructor
                val adapter = UserAdapter(storyItemList)
                binding.rvReview.adapter = adapter
            }
        }
//        viewModel.storyData.observe(this) { dataList ->
//            if (dataList != null) {
//                val adapter = UserAdapter(dataList)
//                binding.rvReview.adapter = adapter
//            }
//
//        }
    }
}