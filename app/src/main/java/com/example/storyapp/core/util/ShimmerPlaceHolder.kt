package com.example.storyapp.core.util

import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable

class ShimmerPlaceHolder {
    companion object{
        fun active(): ShimmerDrawable {
            val shimmer = Shimmer.AlphaHighlightBuilder()
                .setDuration(1500)
                .setBaseAlpha(0.8f)
                .setHighlightAlpha(0.7f)
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build()

            return ShimmerDrawable().apply {
                setShimmer(shimmer)
            }
        }
    }
}