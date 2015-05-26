package com.reconinstruments.ui.examples.carousel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.reconinstruments.ui.list.SimpleListActivity;
import com.reconinstruments.ui.list.SimpleListItem;
import com.reconinstruments.ui.list.StandardListItem;

import java.util.Arrays;
import java.util.List;

public class CarouselExamples extends SimpleListActivity {

    public class ListItem extends StandardListItem {
        Class activityClass;
        public ListItem(String text, Class activityClass){
            super(text);
            this.activityClass = activityClass;
        }
        public void onClick() {
            Context context = CarouselExamples.this;
            context.startActivity(new Intent(context, activityClass));
        }
    }

    SimpleListItem[] items = {
            new ListItem("Image Carousel",ImageCarousel.class),
            new ListItem("Stat Carousel",StatCarousel.class)

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