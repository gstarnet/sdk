package com.reconinstruments.ui.dialog;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.reconinstruments.ui.R;
import com.reconinstruments.ui.carousel.CarouselItem;
import com.reconinstruments.ui.carousel.CarouselViewPager;

import java.util.List;

/**
 * Dialog box containing a carousel, for selecting from a list of options
 */
public abstract class CarouselDialog extends BaseDialog {

    /**
     * The primary view contained in this dialog
     */
    CarouselViewPager carousel;
    /**
     * Carousel selection items that define selection views and behaviour
     */
    protected List<? extends CarouselItem> items;
    /**
     * optional initial selected item index, if not set initializes to item 0
     */
    private int initialSelection;

    OnItemSelectedListener onItemSelectedListener;

    /**
     * Constructor
     * @param context Containing fragment activity
     * @param layout Dialog box layout id, must include R.id.carousel of type com.reconinstruments.ui.carousel.CarouselViewPager
     * @param items List of selectable items
     */
    public CarouselDialog(FragmentActivity context,int layout, List<? extends CarouselItem> items) {
        this(context,layout,items,0);
    }

    /**
     * Constructor
     * @param context Containing fragment activity
     * @param layout Dialog box layout id, must include R.id.carousel of type com.reconinstruments.ui.carousel.CarouselViewPager
     * @param items List of selectable items
     * @param initialSelection Index of selected item
     */
    public CarouselDialog(FragmentActivity context,int layout, List<? extends CarouselItem> items, int initialSelection) {
        super(context,layout);
        this.items = items;
        this.initialSelection = initialSelection;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout outerDialog = new FrameLayout(getActivity());
        View view = inflater.inflate(layout, outerDialog, true);
        carousel = (CarouselViewPager) view.findViewById(R.id.carousel);
        carousel.setBreadcrumbContainer(outerDialog);
        carousel.setContents(this, items, initialSelection);
        carousel.setOnItemSelectedListener(new CarouselViewPager.OnItemSelectedListener() {
            @Override
            public void onItemSelected(CarouselViewPager parent, CarouselItem item, int position) {
                if (onItemSelectedListener != null)
                    onItemSelectedListener.onItemSelected(CarouselDialog.this, item, position);
            }
        });
        updateView(view);
        return outerDialog;
    }

    /**
     * @param onItemSelectedListener callback to be invoked on scrolling to and selecting an item in the dialog,
     *                               uses own CarouselDialog.OnItemSelectedListener in order to make the dialog
     *                               available to the callback, (eg. to perform dialog.dismiss)
     */
    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    /**
     * @return The CarouselViewPager contained in this dialog
     */
    public CarouselViewPager getCarousel() {
        return carousel;
    }

    /**
     * Sets the position of carousel view pager to have an item in view
     * @param position Index of the item to be visible
     */
    public void setCurrentItem(int position) {
        carousel.setCurrentItem(position);
    }

    /**
     * Interface for callbacks on selecting an item, makes the active dialog available to the callback
     */
    public interface OnItemSelectedListener {
        void onItemSelected(CarouselDialog dialog, CarouselItem item, int position);
    }
}
