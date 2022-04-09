package com.example.storyapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.storyapp.databinding.CardBinding
import com.example.storyapp.feature.story.domain.model.ListStoryItem
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable


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
            tvName.text=listStory[position]?.name
            tvDescription.text=listStory[position]?.description
        }
         val shimmer = Shimmer.AlphaHighlightBuilder()
            .setDuration(1500)
            .setBaseAlpha(0.6f)
            .setHighlightAlpha(0.5f)
            .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
            .setAutoStart(true)
            .build()

        val shimmerDrawable = ShimmerDrawable().apply {
            setShimmer(shimmer)
        }
        listStory[position]?.photoUrl?.let {
            Glide.with(holder.itemView.context)
                .load(it)
                .placeholder(shimmerDrawable)
                .transform(CircleCrop())
                .into(holder.binding.ivStory)
        }
        holder.itemView.setOnClickListener {
            listStory[holder.adapterPosition]?.let { onItemClickCallback.onItemClicked(it) }
        }
    }

    override fun getItemCount(): Int = listStory.size
    inner class ListViewHolder(var binding: CardBinding) : RecyclerView.ViewHolder(binding.root)
    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem)
    }
}