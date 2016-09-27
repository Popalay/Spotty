package com.popalay.spotty.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.popalay.spotty.R
import com.popalay.spotty.models.Spot
import kotlinx.android.synthetic.main.item_spot.view.*
import java.util.*

class SpotAdapter  : RecyclerView.Adapter<SpotAdapter.ViewHolder>(){

    var items: MutableList<Spot> = ArrayList()

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        with(holder.itemView) {
            holder.itemView.name.text = item.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_spot, null)
        return ViewHolder(view)
    }

    class ViewHolder(viewItem: View) : RecyclerView.ViewHolder(viewItem)
}