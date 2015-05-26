package com.reconinstruments.ui.carousel;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;
import com.reconinstruments.ui.R;

import java.util.List;

/**
 * Activity class for displaying a carousel view
 */
public abstract class CarouselActivity extends FragmentActivity {

    CarouselViewPager carousel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(getLayoutId());
        carousel = (CarouselViewPager) findViewById(R.id.carousel);

        carousel.setBreadcrumbContainer((ViewGroup)findViewById(android.R.id.content));
        carousel.setContents(this, createContents());
    }

    /**
     * @return CarouselViewPager view contained in this activity
     */
    public CarouselViewPager getCarousel() {
        return carousel;
    }

    /**
     * Override to supply a custom layout layout must include view R.id.carousel
     * of type com.reconinstruments.ui.carousel.CarouselViewPager
     */
    public int getLayoutId() {
        return R.layout.carousel_host;
    }

    /**
     * @return list of CarouselItems that define the views
     */
    protected abstract List<? extends CarouselItem> createContents();
}
