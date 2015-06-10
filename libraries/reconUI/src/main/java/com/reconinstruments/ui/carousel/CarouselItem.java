package com.reconinstruments.ui.carousel;

import android.content.Context;
import android.view.View;

import java.io.Serializable;

/**
 * Serializable object to define views and behaviour for items in a carousel
 *
 * note: because it is serializable, if implemented as an inner class of an activity
 * make it a static class, because the activity cannot be serialized
 * Because Serializable uses reflection, serialization errors can only be caught at runtime
 */
public abstract class CarouselItem implements Serializable {


    public enum POSITION {
        LEFT,CENTER,RIGHT
    }

    /**
     * Called when select is pressed while view is selected
     */
    public void onClick(Context context) {}

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