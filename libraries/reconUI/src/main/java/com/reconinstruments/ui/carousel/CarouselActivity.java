package com.reconinstruments.ui.carousel;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;
import com.reconinstruments.ui.R;

import java.util.Arrays;
import java.util.List;

/**
 * Activity class for displaying a carousel view
 */
public abstract class CarouselActivity extends FragmentActivity {

    CarouselViewPager carousel;

    /**
     * @return CarouselViewPager view contained in this activity
     */
    public CarouselViewPager getCarousel() {
        if(carousel==null) {
            carousel = (CarouselViewPager) findViewById(R.id.carousel);
            carousel.setBreadcrumbContainer((ViewGroup)findViewById(android.R.id.content));
        }
        return carousel;
    }
}
