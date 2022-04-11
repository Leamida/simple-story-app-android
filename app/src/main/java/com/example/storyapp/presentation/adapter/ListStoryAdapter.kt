package com.example.storyapp.presentation.adapter

import android.app.Activity
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
import com.example.storyapp.core.util.ShimmerPlaceHolder


class ListStoryAdapter(
    private val listStory: List<ListStoryItem?>
) : RecyclerView.Adapter<ListStoryAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            CardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.binding.apply {
            tvName.text = listStory[position]?.name
            tvDescription.text = listStory[position]?.description
        }

        listStory[position]?.photoUrl?.let {
            Glide.with(holder.itemView.context)
                .load(it)
                .placeholder(ShimmerPlaceHolder.active())
                .into(holder.binding.ivStory)
        }
        holder.itemView.setOnClickListener {
            listStory[holder.adapterPosition]?.let { onItemClickCallback.onItemClicked(it) }
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, StoryDetailActivity::class.java)
            intent.putExtra("Data", listStory[position])

            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    holder.itemView.context as Activity,
                    Pair(holder.binding.tvName, "tName"),
                    Pair(holder.binding.ivStory, "tIvStory"),
                    Pair(holder.binding.tvDescription, "tDescription"),
                )
            holder.itemView.context.startActivity(intent, optionsCompat.toBundle())
        }
    }

    override fun getItemCount(): Int = listStory.size
    inner class ListViewHolder(var binding: CardBinding) : RecyclerView.ViewHolder(binding.root)
    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem)
    }
}