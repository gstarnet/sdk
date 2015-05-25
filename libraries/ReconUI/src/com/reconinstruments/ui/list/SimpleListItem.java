package com.reconinstruments.ui.list;

import android.view.View;
import android.widget.ListView;

/**
 * Simplified interface to an item in a list view, providing a mechanism for setting the view layout and contents
 * and providing onClick and onSelect behaviour
 */
public abstract class SimpleListItem {
    ListView listView;
    int position;

    public void attachToList(ListView listView,int position) {
        this.listView = listView;
        this.position = position;
    }
    public abstract int getLayoutId();

    /**
     * Update the contents of this items view, view may be reused
     */
    public abstract void updateView(View view);

    /**
     * Retrieve the items view from it's parent listView, doesn't cache the view so that it can be recreated/managed
     * by the parent
     */
    public View getView() {
        return listView.getChildAt(position-listView.getFirstVisiblePosition());
    }
    public void onClick() {};
    public void onSelected() {}
}