package com.reconinstruments.ui.carousel;

import android.view.View;

import java.io.Serializable;

/**
 * Serializable object to define views and behaviour for items in a carousel
 */
public abstract class CarouselItem implements Serializable {


    public enum POSITION {
        LEFT,CENTER,RIGHT
    }

    /**
     * Called when select is pressed while view is selected
     */
    public void onClick() {}

    /**
     * Implement to modify view depending on the relative position in the carousel
     * Can be used to modify selected view (CENTER) or adjacent views depending on screen position
     * @param position Position of the view relative to the selected carousel item
     */
    public void updateViewForPosition(View view,POSITION position) {}

    /**
     * @return Layout id for carousel item
     */
    public abstract int getLayoutId();
    /**
     * Update view contents based on this item
     * @param view View to be updated
     */
    public abstract void updateView(View view);
}