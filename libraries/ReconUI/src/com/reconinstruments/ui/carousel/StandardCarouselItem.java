package com.reconinstruments.ui.carousel;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.reconinstruments.ui.R;
import com.reconinstruments.ui.UIUtils;


/**
 * CarouselItem to be used with views containing at most one dynamic TextView and ImageView
 */
public class StandardCarouselItem extends CarouselItem {

    String title;
    Integer icon;

    public StandardCarouselItem(String title) {
        this(title,null);
    }
    public StandardCarouselItem(int icon) {
        this(null,icon);
    }
    public StandardCarouselItem(String title, Integer icon) {
        this.title = title;
        this.icon = icon;
    }

    /**
     * @return Id for layout containing one or both of a TextView with id R.id.title and an ImageView R.id.icon
     */
    public int getLayoutId() {
        return R.layout.carousel_item_title_icon_column;
    }

    public void updateView(View view) {
        UIUtils.setOptionalText(title, view, R.id.title);
        UIUtils.setOptionalImage(icon, view, R.id.icon);
    }

    public String getTitle() {return title;}
    public Integer getIcon() {return icon;}
}