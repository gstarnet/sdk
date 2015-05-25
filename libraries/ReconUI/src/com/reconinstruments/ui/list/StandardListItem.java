package com.reconinstruments.ui.list;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.reconinstruments.ui.R;
import com.reconinstruments.ui.UIUtils;

/**
 * SimpleListItem implementation to be used with four optional fields, two TextViews and two ImageViews
 * see layouts/list_item_standard_layout for default layout
 */
public class StandardListItem extends SimpleListItem {
    String text;
    String subtitle;
    Integer iconId;
    Integer subIconId;
    int layoutId;

    public StandardListItem(int layoutId, String text, String subtitle, Integer iconId, Integer subIconId) {
        this.layoutId = layoutId;
        this.text = text;
        this.subtitle = subtitle;
        this.iconId = iconId;
        this.subIconId = subIconId;
    }
    public StandardListItem(String text, String subText, Integer iconId, Integer subIconId) {
        this(R.layout.list_item_standard_layout,text,subText,iconId,subIconId);
    }
    public StandardListItem(String text) {
        this(text,null,null,null);
    }
    public StandardListItem(String text, String subText) {
        this(text,subText,null,null);
    }
    public StandardListItem(String text, int iconId) {
        this(text,null,iconId,null);
    }

    @Override
    public int getLayoutId() {
        return layoutId;
    }
    @Override
    public void updateView(View view) {
        UIUtils.setOptionalText(getText(), view, R.id.title);
        UIUtils.setOptionalText(getSubtitle(), view, R.id.subtitle);
        UIUtils.setOptionalImage(getIconId(), view, R.id.icon);
        UIUtils.setOptionalImage(getSubIconId(), view, R.id.subicon);
    }

    public String getText() {return text;}
    public String getSubtitle() {return subtitle;}
    public Integer getIconId() {return iconId;}
    public Integer getSubIconId() {return subIconId;}
}