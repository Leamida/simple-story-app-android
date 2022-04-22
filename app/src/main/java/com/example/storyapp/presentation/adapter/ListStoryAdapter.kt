package com.example.storyapp.presentation.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.databinding.CardBinding
import com.example.storyapp.feature.story.domain.model.ListStoryItem
import com.example.storyapp.presentation.ui.StoryDetailActivity
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.storyapp.core.util.ShimmerPlaceHolder
import dagger.hilt.android.internal.managers.FragmentComponentManager

class ListStoryAdapter
 : PagingDataAdapter<ListStoryItem,ListStoryAdapter.ListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            CardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data,holder.itemView.context)
        }
        data?.let { stories->
            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, StoryDetailActivity::class.java)
                intent.putExtra("Data", stories)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        FragmentComponentManager.findActivity(holder.itemView.context) as Activity,
                        Pair(holder.binding.tvName, "tName"),
                        Pair(holder.binding.ivStory, "tIvStory"),
                        Pair(holder.binding.tvDescription, "tDescription"),
                    )
                holder.itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    inner class ListViewHolder(var binding: CardBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data :ListStoryItem,context: Context){
            binding.tvName.text = data.name
            binding.tvDescription.text = data.description
            Glide.with(context)
                .load(data.photoUrl)
                .placeholder(ShimmerPlaceHolder.active())
                .into(binding.ivStory)
        }
    }
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}