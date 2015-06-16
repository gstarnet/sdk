package com.reconinstruments.ui.carousel;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.reconinstruments.ui.breadcrumb.BreadcrumbToast;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewPager adapter class for CarouselItemFragments
 */
public interface CarouselPagerAdapter {
    View getView(int position);
    CarouselItem getCarouselItem(int position);

    void updateViewForPosition(int position, CarouselItem.POSITION rel_position);
}
