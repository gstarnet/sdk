package com.reconinstruments.ui.examples.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.reconinstruments.ui.list.SimpleListActivity;
import com.reconinstruments.ui.list.SimpleListItem;
import com.reconinstruments.ui.list.StandardListItem;

import java.util.Arrays;
import java.util.List;

public class ListExamples extends SimpleListActivity {

    public class ListItem extends StandardListItem {
        Class activityClass;
        public ListItem(String text, Class activityClass){
            super(text);
            this.activityClass = activityClass;
        }
        public void onClick() {
            Context context = ListExamples.this;
            context.startActivity(new Intent(context, activityClass));
        }
    }

    SimpleListItem[] items = {
            new ListItem("List item types",ListItemTypes.class),
            new ListItem("Fixed list with info",ListWithInfoActivity.class)
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public List<SimpleListItem> createContents() {
        return Arrays.asList(items);
    }
}