package com.example.stroryapp.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stroryapp.R
import com.example.stroryapp.activity.DetailActivity
import com.example.stroryapp.data.pref.UserModel
import com.example.stroryapp.databinding.ItemRowBinding
import com.example.stroryapp.response.ListStoryItem

class UserAdapter (private val dataList: List<ListStoryItem>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemRowBinding): RecyclerView.ViewHolder(binding.root) {
        // Bind your data here
        fun bind(review: ListStoryItem) {
            binding.tvItemName.text = review.name
            binding.deskripsi.text = review.description
            Glide.with(itemView.context)
                .load(review.photoUrl)
                .into(binding.imgDetailPhoto)
            itemView.setOnClickListener {
                val intentDetail = Intent(itemView.context, DetailActivity::class.java)
                intentDetail.putExtra( DetailActivity.EXTRA_ID, review.id)
                itemView.context.startActivity(intentDetail)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = dataList[position]
        holder.bind(review)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}