package com.reconinstruments.ui.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/*
 * ArrayAdapter for list of SimpleListItems, which define their own views, uses convertView for all views
 * so must extend and implement getViewTypeCount and getItemViewType in order to use more than one layout
 */
public class SimpleArrayAdapter<T extends SimpleListItem> extends ArrayAdapter<T> {

    Context context = null;
    List<T> contents = null;

    public SimpleArrayAdapter(Context context, List<T> contents) {
        super(context, 0, contents);
        this.context = context;
        this.contents = contents;
    }

    @Override
    public T getItem(int position) {
        return contents.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        T item = getItem(position);
        if(convertView==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(item.getLayoutId(), null);
        }
        item.updateView(convertView);
        return convertView;
    }
}