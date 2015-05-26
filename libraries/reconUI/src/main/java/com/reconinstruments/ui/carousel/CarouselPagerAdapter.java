package com.reconinstruments.ui.carousel;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import com.reconinstruments.ui.breadcrumb.BreadcrumbToast;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewPager adapter class for CarouselItemFragments
 */
public class CarouselPagerAdapter extends FragmentStatePagerAdapter {

    final String TAG = CarouselPagerAdapter.class.getSimpleName();

    private List<CarouselItemFragment> fragments;
    private ViewPager mPager;

    public CarouselPagerAdapter(FragmentManager fm, List<? extends CarouselItem> items, ViewPager mPager) {
        super(fm);

        fragments = new ArrayList<CarouselItemFragment>();
        for(int i=0;i<items.size();i++) {
            fragments.add(CarouselItemFragment.newInstance(items.get(i),i,items.size()));
        }
        this.mPager = mPager;
    }

    @Override
    public CarouselItemFragment getItem(int position) {
        return fragments.get(position);
    }
    public CarouselItemFragment getCurrentItem() {
        if(fragments.size()==0)
            return null;
        return fragments.get(mPager.getCurrentItem());
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public float getPageWidth(int position) {
        return 1.0f;
    }
}
