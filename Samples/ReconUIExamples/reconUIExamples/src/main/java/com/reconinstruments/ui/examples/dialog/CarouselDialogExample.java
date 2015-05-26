package com.reconinstruments.ui.examples.dialog;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import com.reconinstruments.ui.carousel.CarouselItem;
import com.reconinstruments.ui.carousel.CarouselItemFragment;
import com.reconinstruments.ui.carousel.CarouselViewPager;
import com.reconinstruments.ui.carousel.StandardCarouselItem;
import com.reconinstruments.ui.dialog.CarouselDialog;
import com.reconinstruments.ui.dialog.DialogBuilder;
import com.reconinstruments.ui.examples.R;

import java.util.Arrays;

/**
 * Created by chris on 06/05/15.
 */
public class CarouselDialogExample extends CarouselDialog implements DialogBuilder.ViewCallback {

    static CarouselItem[] selections = {
            new CheckedSelectionItem("Option 1"),
            new CheckedSelectionItem("Option 2"),
            new CheckedSelectionItem("Option 3")
    };
    OnItemSelectedListener onItemSelectedListener;

    public static class CheckedSelectionItem extends StandardCarouselItem {
        public CheckedSelectionItem(String title) {
            super(title);
        }
        @Override
        public int getLayoutId() {
            return R.layout.carousel_item_hidden_checkmark;
        }
    }

    public CarouselDialogExample(FragmentActivity context,int initialSelection,OnItemSelectedListener onItemSelectedListener) {
        super(context,R.layout.custom_carousel_host, Arrays.asList(selections), initialSelection);
        this.onItemSelectedListener = onItemSelectedListener;
    }

    @Override
    public void onStart() {
        super.onStart();
        setOnItemSelectedListener(onItemSelectedListener);
        getCarousel().setOnPageSelectListener(new CarouselViewPager.OnPageSelectListener() {
            @Override
            public void onPageSelected(int position) {
                updateView();
            }
        });
    }

    @Override
    public void updateView(View view) {
        TextView titleView = (TextView) view.findViewById(R.id.title);

        CarouselItemFragment itemFragment = getCarousel().getAdapter().getCurrentItem();
        StandardCarouselItem item = (StandardCarouselItem) itemFragment.getItem();
        int position = itemFragment.getPosition();
        titleView.setText(item.getTitle());

        TextView subtitleView = (TextView) view.findViewById(R.id.subtitle);
        subtitleView.setText((position+1)+"/"+selections.length);
    }
}
