package com.reconinstruments.ui.list;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import com.reconinstruments.ui.R;

import java.util.List;

/**
 * ListActivity for a list of SimpleListItems, which can define their own view and onClick behaviour
 */
public abstract class SimpleListActivity extends BaseListActivity {

    SimpleArrayAdapter<SimpleListItem> mListAdapter;

    public List<SimpleListItem> attachContents() {
        List<SimpleListItem> contents = createContents();
        for(int i=0;i<contents.size();i++) {
            contents.get(i).attachToList(getListView(),i);
        }
        return contents;
    }
    public SimpleArrayAdapter<SimpleListItem> createAdapter(List<SimpleListItem> contents) {
        return new SimpleArrayAdapter<SimpleListItem>(this,contents);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListAdapter.getItem(position).onClick();
            }
        });
        mListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mListAdapter.getItem(position).onSelected();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mListAdapter = createAdapter(attachContents());
        mListView.setAdapter(createAdapter(attachContents()));
    }

    public SimpleArrayAdapter<SimpleListItem> getAdapter() {
        return mListAdapter;
    }

    /**
     * Helper function to refresh view for a specific list item
     * @param item item to update view for
     */
    public void updateItem(SimpleListItem item) {
        int position = mListAdapter.getPosition(item);
        View view = getListView().getChildAt(position);
        getAdapter().getView(position, view, getListView());
    }

    public int getLayoutId() {
        return R.layout.list_standard_layout;
    }

    /**
     * @return List of SimpleListItems to be shown in the list
     */
    public abstract List<SimpleListItem> createContents();
}