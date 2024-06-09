package com.example.stroryapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stroryapp.R
import com.example.stroryapp.data.Result
import com.example.stroryapp.data.UserAdapter
import com.example.stroryapp.databinding.ActivityMainBinding
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
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                viewModel.getStory(user.token).observe(this) { it ->
                    if (it != null) {
                        when (it) {
                            is Result.Loading -> {
                                // Tampilkan progress bar atau indikator loading lainnya
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                // Sembunyikan progress bar
                                binding.progressBar.visibility = View.GONE

                                // Ambil data story dan tampilkan dalam RecyclerView
                                val adapter = UserAdapter(it.data.listStory!!) { clickedStory ->
                                    // Handle item click here if needed
                                    val intentDetail = Intent(this@MainActivity, DetailActivity::class.java)
                                    intentDetail.putExtra(DetailActivity.EXTRA_ID, clickedStory.id)
                                    startActivity(intentDetail)
                                }
                                binding.rvReview.layoutManager = LinearLayoutManager(this)
                                binding.rvReview.adapter = adapter
                                binding.halo.text = resources.getString(R.string.greeting)
                                Log.d("nama pengguna ", "onCreate: ${user.id}")
                            }
                            is Result.Error -> {
                                // Tangani kesalahan, seperti menampilkan pesan kesalahan
                                Log.e("MainActivity", "Error: ${it.error}")
                                // Atau tampilkan pesan kesalahan pada UI
                                Toast.makeText(this, "Error: ${it.error}", Toast.LENGTH_SHORT).show()
                            }
                        }
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
