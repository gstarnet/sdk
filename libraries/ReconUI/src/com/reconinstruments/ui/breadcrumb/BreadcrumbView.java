package com.reconinstruments.ui.breadcrumb;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import com.reconinstruments.ui.R;
import com.reconinstruments.ui.UIUtils;

import java.util.ArrayList;

/*
 View class for creating a horizontal or vertical breadcrumb pager indicator
 Should not be instantiated on it's own, use BreadcrumbToast to display

 */
public class BreadcrumbView extends LinearLayout {

    private static final String TAG = "BreadcrumbView";

    private static final int HORIZONTAL_BREADCRUMB_HEIGHT = 4;
    private static final int VERTICAL_BREADCRUMB_WIDTH = 4;

    private static final int HORIZONTAL_BREADCRUMB_PADDING = 4;
    private static final int VERTICAL_BREADCRUMB_PADDING = 2;

    ArrayList<ImageView> imageList = new ArrayList<ImageView>();

    public BreadcrumbView(Context context, boolean horizontal, int position, int numIcons) {
        super(context);

        this.setOrientation(horizontal ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
        if(horizontal){
            this.setPadding(HORIZONTAL_BREADCRUMB_PADDING, 0, HORIZONTAL_BREADCRUMB_PADDING, 0);
        }
        this.setGravity(Gravity.CENTER);

        for(int i=0; i<numIcons; i++) {
            ImageView imgView = new ImageView(context);
            int breadcrumbWidth = getBreadcrumbWidth(horizontal,numIcons);
            int breadcrumbHeight = getBreadcrumbHeight(horizontal,numIcons);
            imgView.setLayoutParams(new LinearLayout.LayoutParams(breadcrumbWidth, breadcrumbHeight));
            imgView.setImageResource((position == i) ? R.drawable.breadcrumb_square_white : R.drawable.breadcrumb_square_dark);
            imgView.setScaleType(ScaleType.FIT_XY);
            if(horizontal){
                imgView.setPadding(HORIZONTAL_BREADCRUMB_PADDING, 0, HORIZONTAL_BREADCRUMB_PADDING, 0);
            }else{
                imgView.setPadding(0, VERTICAL_BREADCRUMB_PADDING, 0, VERTICAL_BREADCRUMB_PADDING);
            }

            imageList.add(imgView);
            this.addView(imageList.get(i));
        }
    }
    private int getBreadcrumbWidth(boolean horizontal, int numIcons) {
        return horizontal ?
                UIUtils.JET_SCREEN_WIDTH/numIcons :
                VERTICAL_BREADCRUMB_WIDTH;
    }
    private int getBreadcrumbHeight(boolean horizontal, int numIcons) {
        return horizontal ?
                HORIZONTAL_BREADCRUMB_HEIGHT :
                (UIUtils.JET_WINDOW_HEIGHT/numIcons);
    }

    public void updatePosition(int position){
        for(int i=0; i<imageList.size(); i++){
            imageList.get(i).setImageResource((position == i) ?
                    R.drawable.breadcrumb_square_white :
                    R.drawable.breadcrumb_square_dark);
        }
    }
}