package com.example.storyapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.data.dataClass.StoryItem
import com.example.storyapp.databinding.CardstoryBinding
import com.example.storyapp.ui.DetailActivity

class StoryListAdapter :
    PagingDataAdapter<StoryItem, StoryListAdapter.MyViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = CardstoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }


    class MyViewHolder(private val binding: CardstoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StoryItem) {
            binding.apply {
                tvItemName.text = data.name
                tvDescription.text = data.description
                imgItemPhoto.setImageResource(R.drawable.avatar)
                Glide.with(itemView.context)
                    .load(data.photoUrl)
                    .into(imgItemStory)
            }
            itemView.setOnClickListener {
                val intentDetail = Intent(itemView.context, DetailActivity::class.java)
                intentDetail.putExtra("STORY", data)
                itemView.context.startActivity(intentDetail)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryItem>() {
            override fun areItemsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}