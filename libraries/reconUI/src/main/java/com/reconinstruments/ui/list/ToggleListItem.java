package com.reconinstruments.ui.list;

import android.widget.ImageView;
import com.reconinstruments.ui.R;

/**
 * StandardListItem with a toggleable checkbox in the 'subicon' position
 */
public class ToggleListItem extends StandardListItem {
    boolean enabled;
    public ToggleListItem(String text,boolean enabled){
        super(text);
        this.enabled = enabled;
    }
    @Override
    public void onClick() {
        this.enabled = !enabled;
        ImageView subIcon = (ImageView) getView().findViewById(R.id.subicon);
        subIcon.setImageResource(getSubIconId());
    }
    @Override
    public Integer getSubIconId() {
        return enabled ? R.drawable.selectable_checkbox_enabled:R.drawable.selectable_checkbox_disabled;
    }
}