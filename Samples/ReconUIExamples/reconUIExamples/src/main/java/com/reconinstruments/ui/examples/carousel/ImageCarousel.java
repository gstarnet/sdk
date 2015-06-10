package com.reconinstruments.ui.examples.carousel;

import android.content.Context;
import android.content.Intent;
import com.reconinstruments.ui.carousel.CarouselActivity;
import com.reconinstruments.ui.carousel.CarouselItem;
import com.reconinstruments.ui.carousel.StandardCarouselItem;
import com.reconinstruments.ui.examples.ActionBarExample;
import com.reconinstruments.ui.examples.R;

import java.util.Arrays;
import java.util.List;

public class ImageCarousel extends CarouselActivity {

    @Override
    public int getLayoutId() {
        return R.layout.carousel_host;
    }

    static class ImageCarouselItem extends StandardCarouselItem {
        public ImageCarouselItem(String title, Integer icon) {
            super(title, icon);
        }
        @Override
        public int getLayoutId() {
            return R.layout.carousel_item_title_icon_column;
        }
        @Override
        public void onClick(Context context) {
            context.startActivity(new Intent(context, ActionBarExample.class));
        }
    }
    CarouselItem[] items = {
            new ImageCarouselItem("New Activity",R.drawable.carousel_icon_newactivity),
            new ImageCarouselItem("My Apps",R.drawable.carousel_icon_myapps),
            new ImageCarouselItem("Cycling",R.drawable.carousel_icon_cycling),
            new ImageCarouselItem("Running",R.drawable.carousel_icon_running),
            new ImageCarouselItem("Settings",R.drawable.carousel_icon_settings)
    };

    @Override
    public List<? extends CarouselItem> createContents() {
        return Arrays.asList(items);
    }
}
