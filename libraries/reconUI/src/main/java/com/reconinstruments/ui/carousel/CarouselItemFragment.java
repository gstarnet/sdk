package com.reconinstruments.ui.carousel;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import com.reconinstruments.ui.R;

/**
 * Fragment class to contain a CarouselItem
 */
public class CarouselItemFragment extends Fragment {

    private static final String ARG_ITEM = "ITEM";
    private static final String ARG_POSITION = "POSITION";

    private boolean loaded = false;
    private CarouselItem carouselItem;
    private int position;

    private CarouselItem.POSITION initialPosition = null;

    /**
     * Used to instantiate a CarouselItemFragment, constructors are reserved to be created by Android
     * @param carouselItem CarouselItem contained in the fragment
     * @param position position in the containing CarouselViewPager
     * @param numItems number of CarouselItems in the CarouselViewPager
     * @return newly created fragment
     */
    public static CarouselItemFragment newInstance(CarouselItem carouselItem,int position,int numItems) {
        CarouselItemFragment fragment = new CarouselItemFragment();
        Bundle args = new Bundle(3);
        args.putSerializable(ARG_ITEM, carouselItem);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadArgs();
    }

    /**
     * Load the CarouselItem from the fragments arguments, after the fragment is created, as a new instance
     * or when recreated
     */
    public void loadArgs() {
        if(!loaded) {
            carouselItem = (CarouselItem) getArguments().getSerializable(ARG_ITEM);
            position = getArguments().getInt(ARG_POSITION);
            loaded = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout fragmentContainer = new FrameLayout(getActivity());
        fragmentContainer.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        View view = inflater.inflate(carouselItem.getLayoutId(), fragmentContainer, true);
        carouselItem.updateView(view);
        return fragmentContainer;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if(initialPosition!=null)
            updateViewForPosition(initialPosition);
    }

    /**
     * updates the relative position of the fragment, calls through to CarouselItem.updateViewForPosition
     */
    public void updateViewForPosition(CarouselItem.POSITION position) {
        View view = getView();
        if(view==null) {
            // view has not been initialized yet, store position to be applied after view is created
            initialPosition = position;
            return;
        }

        if(position==CarouselItem.POSITION.CENTER)
            view.setSelected(true);
        else
            view.setSelected(false);

        carouselItem.updateViewForPosition(view,position);
    }

    public CarouselItem getItem() {
        if(carouselItem==null)
            loadArgs();
        return carouselItem;
    }

    /**
     * @return The position of the fragment in it's adapter
     */
    public int getPosition() {
        return position;
    }
}
