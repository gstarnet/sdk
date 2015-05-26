package com.reconinstruments.ui.examples;

import android.content.Context;
import android.content.Intent;
import com.reconinstruments.ui.examples.carousel.CarouselExamples;
import com.reconinstruments.ui.examples.dialog.DialogExamples;
import com.reconinstruments.ui.examples.list.ListExamples;
import com.reconinstruments.ui.list.SimpleListActivity;
import com.reconinstruments.ui.list.SimpleListItem;
import com.reconinstruments.ui.list.StandardListItem;

import java.util.Arrays;
import java.util.List;

public class MainExampleList extends SimpleListActivity {


    public class ListItem extends StandardListItem {
        Class activityClass;
        public ListItem(String text, Class activityClass){
            super(text);
            this.activityClass = activityClass;
        }
        public void onClick() {
            Context context = MainExampleList.this;
            context.startActivity(new Intent(context, activityClass));
        }
    }

    SimpleListItem[] items = {
            new ListItem("Carousel Examples",CarouselExamples.class),
            new ListItem("List Examples",ListExamples.class),
            new ListItem("Dialog Examples",DialogExamples.class),
            new ListItem("Text Examples",TextExamples.class),
            new ListItem("Action Bar Example",ActionBarExample.class)
    };

    @Override
    public List<SimpleListItem> createContents() {
        return Arrays.asList(items);
    }
}