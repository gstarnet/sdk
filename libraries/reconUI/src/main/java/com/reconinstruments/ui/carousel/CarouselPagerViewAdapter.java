package com.reconinstruments.ui.carousel;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * ViewPager adapter class for CarouselItemFragments
 */
public class CarouselPagerViewAdapter extends PagerAdapter {

    LayoutInflater inflater;

    List<? extends CarouselItem> items;
    View[] views;

    public CarouselPagerViewAdapter(Context context,List<? extends CarouselItem> items) {
        this.items = items;
        inflater = LayoutInflater.from(context);
        views = new View[items.size()];
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = getView(position);
        ((ViewPager) container).addView(view, position);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        ((ViewPager) collection).removeView((View)view);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==((View)object);
    }

    public View getView(int position) {
        View view = views[position];
        if(view==null) {
            CarouselItem carouselItem = items.get(position);
            view = inflater.inflate(carouselItem.getLayoutId(), null);
            carouselItem.updateView(view);
            views[position] = view;
        }
        return view;
    }

    public CarouselItem getCarouselItem(int position) {
        return items.get(position);
    }

    public void updateViewForPosition(int position, CarouselItem.POSITION rel_position) {

        View view = getView(position);
        CarouselItem carouselItem = items.get(position);

        if(rel_position==CarouselItem.POSITION.CENTER)
            view.setSelected(true);
        else
            view.setSelected(false);

        carouselItem.updateViewForPosition(view, rel_position);
    }
}
