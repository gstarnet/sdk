package com.reconinstruments.ui.list;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Simple ListActivity built on FragmentActivity containing a circular list
 */
public abstract class BaseListActivity extends FragmentActivity {

    protected ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(getLayoutId());

        mListView = (ListView) findViewById(android.R.id.list);
        mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mListView.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    int selectedItemPos = mListView.getSelectedItemPosition();
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        if(selectedItemPos==(mListView.getAdapter().getCount() - 1)) {
                            mListView.smoothScrollToPosition(0);
                            mListView.setSelection(0);
                        }
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        if (selectedItemPos == 0) {
                            mListView.smoothScrollToPosition(mListView.getAdapter().getCount() - 1);
                            mListView.setSelection(mListView.getAdapter().getCount() - 1);
                        }
                    }
                }
                return false;
            }
        });
    }

    public ListView getListView() {
        return mListView;
    }

    /**
     * @return layout containing android.R.id.list
     */
    public abstract int getLayoutId();
}
