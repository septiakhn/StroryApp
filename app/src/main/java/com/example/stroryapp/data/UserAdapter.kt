package com.example.stroryapp.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stroryapp.databinding.ItemRowBinding
import com.example.stroryapp.response.ListStoryItem

class UserAdapter(private val storyList: List<ListStoryItem?>, private val onItemClick: (ListStoryItem) -> Unit) :
    RecyclerView.Adapter<UserAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(storyList[position])
    }

    override fun getItemCount(): Int {
        return storyList.size
    }

    inner class MyViewHolder(private val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            binding.tvItemName.text = story.name
            binding.deskripsi.text = story.description
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(binding.imgDetailPhoto)
            itemView.setOnClickListener {
                onItemClick(story)
            }
        }
    }
}
