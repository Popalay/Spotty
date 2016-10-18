package com.popalay.spotty.utils.ui;

public interface StickyHeaderAdapter {

    /**
     * Returns the header id for the item at the given position.
     *
     * @param position the item position
     * @return the header id
     */
    long getHeaderId(int position);
}