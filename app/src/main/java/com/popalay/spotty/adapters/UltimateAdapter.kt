package com.popalay.spotty.adapters

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


abstract class UltimateAdapter<T : RecyclerView.ViewHolder> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected var mLayoutInflater: LayoutInflater? = null
    private var mFooterVisibility = false
    private var mHeaderVisibility = true

    interface HeaderInterface {
        fun getHeaderVH(v: View): HeaderVH
        fun getHeaderViewResId(): Int
        fun bindHeaderVH(vh: RecyclerView.ViewHolder)

    }

    interface FooterInterface {
        fun getFooterVH(v: View): FooterVH
        fun getFooterViewResId(): Int
        fun bindFooterVH(vh: RecyclerView.ViewHolder)

    }

    init {
        this.setHasStableIds(true)
    }

    abstract fun clear()

    abstract fun getDataSize(): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.context)
        }
        return when (viewType) {
            HEADER_TYPE_ID -> {
                thisHeader.getHeaderVH(getViewById(thisHeader.getHeaderViewResId(), parent))
            }
            FOOTER_TYPE_ID -> {
                thisFooter.getFooterVH(getViewById(thisFooter.getFooterViewResId(), parent))
            }
            else -> {
                val v = getViewById(getDataViewResId(viewType), parent)
                getDataViewHolder(v, viewType)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var dataPosition = position
        if (dataPosition == 0 && withHeader()) {
            // bind header
            thisHeader.bindHeaderVH(holder)
            (holder as HeaderVH).hideHeader(!mHeaderVisibility)
        } else if (dataPosition == footerPosition && withFooter()) {
            // bind footer
            thisFooter.bindFooterVH(holder)
            (holder as FooterVH).hideFooter(!mFooterVisibility)
        } else {
            // bind data
            if (withHeader()) {
                dataPosition -= 1
            }
            bindDataVH(holder as T, dataPosition)
        }
    }

    override fun getItemViewType(absolutePosition: Int): Int {
        if (absolutePosition == 0 && withHeader()) {
            return HEADER_TYPE_ID
        } else if (withFooter() && absolutePosition == footerPosition) {
            return FOOTER_TYPE_ID
        } else {
            val dataType = getDataViewType(convertAbsolutePositionToData(absolutePosition))
            if (dataType == HEADER_TYPE_ID || dataType == FOOTER_TYPE_ID) {
                throw IllegalArgumentException("Data type can't be $dataType, this value is reserved")
            }
            return dataType
        }
    }

    override fun getItemId(position: Int): Long {
        if (position == 0 && withHeader()) {
            return -111L
        } else if (withFooter() && position == footerPosition) {
            return -222L
        } else {
            return getDataId(if (withHeader()) position - 1 else position)
        }
    }

    override fun getItemCount(): Int {
        var result = 0
        if (withFooter()) {
            result++
        }
        if (withHeader()) {
            result++
        }
        result += getDataSize()
        return result
    }

    fun withFooter() = this is FooterInterface

    fun withHeader() = this is HeaderInterface

    @LayoutRes
    abstract fun getDataViewResId(viewType: Int): Int

    abstract fun getDataId(dataPosition: Int): Long

    abstract fun getDataViewType(dataPosition: Int): Int

    abstract fun getDataViewHolder(v: View, dataViewType: Int): T

    abstract fun bindDataVH(vh: T, dataPosition: Int)

    fun getDataPosition(generalPosition: Int): Int {
        if (withHeader()) {
            return generalPosition - 1
        }
        return generalPosition
    }

    fun setFooterVisibility(visible: Boolean) {
        mFooterVisibility = visible
        notifyDataSetChanged()
    }

    fun setHeaderVisibility(visible: Boolean) {
        mHeaderVisibility = visible
        notifyDataSetChanged()
    }

    fun hasData() = getDataSize() > 0

    protected fun <K> getItemByViewHolder(vh: RecyclerView.ViewHolder, items: List<K>): K? {
        val dataPosition = getDataPosition(vh.adapterPosition)
        if (vh.adapterPosition == RecyclerView.NO_POSITION) {
            return null
        }
        return items[dataPosition]
    }

    protected fun convertAbsolutePositionToData(absolutePosition: Int): Int {
        var result = absolutePosition
        if (withHeader()) {
            result--
        }
        return result
    }

    protected val footerPosition: Int
        get() {
            var result = 0
            if (withHeader()) {
                result++
            }
            result += getDataSize()
            return result
        }

    private fun getViewById(@LayoutRes id: Int, parent: ViewGroup): View {
        return mLayoutInflater!!.inflate(id, parent, false)
    }

    private val thisFooter: FooterInterface
        get() = this as FooterInterface

    private val thisHeader: HeaderInterface
        get() = this as HeaderInterface

    abstract class FooterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun hideFooter(hide: Boolean)
    }

    abstract class HeaderVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun hideHeader(hide: Boolean)
    }

    companion object {
        val HEADER_TYPE_ID = -1
        val FOOTER_TYPE_ID = -2
    }

}

class ViewHolder(viewItem: View) : RecyclerView.ViewHolder(viewItem)

class HeaderViewHolder(val viewItem: View) : UltimateAdapter.HeaderVH(viewItem) {
    override fun hideHeader(hide: Boolean) {
        viewItem.visibility = if (hide) View.GONE else View.VISIBLE
    }
}

class FooterViewHolder(val viewItem: View) : UltimateAdapter.FooterVH(viewItem) {
    override fun hideFooter(hide: Boolean) {
        viewItem.visibility = if (hide) View.GONE else View.VISIBLE
    }
}