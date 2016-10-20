package com.popalay.spotty.utils.ui

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

interface StickyHeaderAdapter {

    /**
     * Returns the header id for the item at the given position.

     * @param position the item position
     * *
     * @return the header id
     */
    fun getHeaderId(position: Int): Long

    fun createStickyHeaderViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    fun bindStickyHeaderViewHolder(holder: RecyclerView.ViewHolder, position: Int)
}