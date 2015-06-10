package com.reconinstruments.ui.carousel;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;
import android.widget.TextView;
import com.reconinstruments.ui.R;
import com.reconinstruments.ui.UIUtils;
import com.reconinstruments.ui.breadcrumb.BreadcrumbToast;

import java.lang.reflect.Field;
import java.util.List;

/*
 * Custom horizontal view pager containing views defined by a list of CarouselItems
 */
public class CarouselViewPager extends ViewPager {
    private static final String TAG = CarouselViewPager.class.getSimpleName();

    // Do not show breadcrumbs if fewer fragments than this
    static final int MIN_NUM_FRAGMENTS_TO_SHOW_BREADCRUMBS = 3;

    // Adapter for CarouselItemFragments
    CarouselPagerAdapter mPagerAdapter;

    // configurable attributes
    // whether to display a horizontal breadcrumb view when the page is changed
    private boolean showBreadcrumbs;
    // show zoom in/out animation on item selection
    private boolean animateSelection;
    // margin between pages
    private int pageMargin;
    // width of active page, will pad active page with remaining width
    private int pageWidth;

    // parent view to attach breadcrumbs to
    private ViewGroup breadcrumbContainer;
    private BreadcrumbToast breadcrumbToast;

    // time to scroll between pages, in ms
    static final int SCROLL_SPEED = 300;

    // listener for when a page is scrolled to
    OnPageSelectListener onPageSelectListener;
    OnPageChangeListener onPageChangeListener;
    // listener for when user presses select on an item
    OnItemSelectedListener onItemSelectedListener;

    /**
     * Simplified onPageChangeListener, called when there is a new active page
     */
    public interface OnPageSelectListener {
        void onPageSelected(int position);
    }
    /**
     * Called when the select button pressed on the active page
     */
    public interface OnItemSelectedListener {
        void onItemSelected(CarouselViewPager parent, CarouselItem item, int position);
    }

    public CarouselViewPager(Context context) {
        super(context);
        init();
    }
    public CarouselViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CarouselViewPager,0,0);
        try {
            showBreadcrumbs = a.getBoolean(R.styleable.CarouselViewPager_showBreadcrumbs, true);
            animateSelection = a.getBoolean(R.styleable.CarouselViewPager_animateSelection, false);
            pageMargin = a.getDimensionPixelSize(R.styleable.CarouselViewPager_pageMargin, 0);
            pageWidth = a.getDimensionPixelSize(R.styleable.CarouselViewPager_pageWidth, 250);
        } finally {
            a.recycle();
        }
        init();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        int horizontalPadding = (width-pageWidth)/2;
        setPadding(horizontalPadding, 0, horizontalPadding, 0);
    }

    public void init() {
        setPageMargin(pageMargin);
        setClipToPadding(false);

        // Use reflection to set fixed scroll speed
        Interpolator sInterpolator = new DecelerateInterpolator();
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(getContext(), sInterpolator, SCROLL_SPEED);
            mScroller.set(this, scroller);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        super.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (onPageChangeListener != null)
                    onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (onPageChangeListener != null)
                    onPageChangeListener.onPageScrollStateChanged(state);
            }

            @Override
            public void onPageSelected(int position) {
                if (onPageChangeListener != null)
                    onPageChangeListener.onPageSelected(position);
                if (onPageSelectListener != null)
                    onPageSelectListener.onPageSelected(position);
                CarouselViewPager.this.onPageSelected(position);
            }
        });
    }

    public CarouselPagerAdapter getAdapter() {
        return mPagerAdapter;
    }

    public void setBreadcrumbContainer(ViewGroup breadcrumbContainer) {
        this.breadcrumbContainer = breadcrumbContainer;
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }
    public void setOnPageSelectListener(OnPageSelectListener onPageSelectListener) {
        this.onPageSelectListener = onPageSelectListener;
    }
    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_DPAD_CENTER) {
            CarouselItemFragment currentFragment = getAdapter().getCurrentItem();
            CarouselItem currentItem = currentFragment.getItem();
            if(animateSelection) {
                currentFragment.getView().startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.carousel_item_push));
            }
            currentItem.onClick(getContext());
            if(onItemSelectedListener!=null) {
                onItemSelectedListener.onItemSelected(this,currentItem, currentFragment.getPosition());
            }
        }
        return super.onKeyUp(keyCode,event);
    }

    private void onPageSelected(int selection) {
        if(breadcrumbToast!=null)
            breadcrumbToast.updateBreadcrumbs(selection);

        getAdapter().getItem(selection).updateViewForPosition(CarouselItem.POSITION.CENTER);
        if (selection>0)
            getAdapter().getItem(selection-1).updateViewForPosition(CarouselItem.POSITION.LEFT);
        if (selection<getAdapter().getCount()-1)
            getAdapter().getItem(selection+1).updateViewForPosition(CarouselItem.POSITION.RIGHT);
    }

    /**
     * Set the items to be displayed and finish initializing the view
     * @param fragment parent fragment of this view pager
     * @param items items to display
     * @param initialSelection item to be initially selected
     */
    public void setContents(Fragment fragment,List<? extends CarouselItem> items,int initialSelection){
        setContents(fragment.getChildFragmentManager(), fragment.getActivity(), items,initialSelection);
    }
    /**
     * Set the items to be displayed and finish initializing the view
     * @param fragment parent fragment of this view pager
     * @param items items to display
     */
    public void setContents(Fragment fragment,List<? extends CarouselItem> items){
        setContents(fragment.getChildFragmentManager(), fragment.getActivity(), items,0);
    }
    /**
     * Set the items to be displayed and finish initializing the view
     * @param activity fragment activity containing this view pager (only use if there is no child fragment within the
     *                 activity containing this view)
     * @param items items to display
     * @param initialSelection item to be initially selected
     */
    public void setContents(FragmentActivity activity,List<? extends CarouselItem> items,int initialSelection){
        setContents(activity.getSupportFragmentManager(),activity,items,initialSelection);
    }
    /**
     * Set the items to be displayed and finish initializing the view
     * @param activity fragment activity containing this view pager (only use if there is no child fragment within the
     *                 activity containing this view)
     * @param items items to display
     */
    public void setContents(FragmentActivity activity,List<? extends CarouselItem> items){
        setContents(activity.getSupportFragmentManager(), activity, items, 0);
    }

    private void setContents(FragmentManager fm,FragmentActivity activity,List<? extends CarouselItem> items,int initialSelection){
        mPagerAdapter = new CarouselPagerAdapter(fm, items, this);
        setAdapter(mPagerAdapter);

        if(showBreadcrumbs&&items.size()>=MIN_NUM_FRAGMENTS_TO_SHOW_BREADCRUMBS) {
            breadcrumbToast = new BreadcrumbToast(activity,breadcrumbContainer,true,items.size());
        }
        setCurrentItem(initialSelection);
        onPageSelected(initialSelection);
    }

    /**
     * Scrolls the pager at a fixed speed
     */
    public class FixedSpeedScroller extends Scroller {
        private int mDuration;
        public FixedSpeedScroller(Context context, Interpolator interpolator, int duration) {
            super(context, interpolator);
            this.mDuration = duration;
        }
        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }
}