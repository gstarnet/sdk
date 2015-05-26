package com.reconinstruments.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.reconinstruments.ui.R;
import com.reconinstruments.ui.UIUtils;

/**
 * ButtonActionView sets the default layout to indicate an action associated with a button or user input
 * Can dynamically set image for common ButtonTypes
 *
 * Attribute actionText sets the text associated with the action
 * Set one of buttonType or buttonIcon to provide an icon, only use buttonIcon if not using one of the predefined
 * input types
 */
public class ButtonActionView extends LinearLayout {

    /**
     * Defined input action types associated with a specific icon
     */
    public enum ButtonType {
        SELECT,
        BACK,
        UP,
        DOWN,
        UPDOWN
    }

    public ButtonActionView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray resArray = context.obtainStyledAttributes(attrs, R.styleable.ButtonActionView, 0, 0);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.button_action, this, true);

        ImageView buttonImage = (ImageView) findViewById(R.id.button_action_icon);
        TextView buttonText = (TextView) findViewById(R.id.button_action_title);

        String actionText = resArray.getString(R.styleable.ButtonActionView_actionText);
        int buttonIcon = resArray.getResourceId(R.styleable.ButtonActionView_buttonIcon, -1);
        int buttonType = resArray.getInt(R.styleable.ButtonActionView_buttonType, -1);

        buttonText.setText(actionText);
        if(buttonIcon != -1) {
            if(buttonType!=-1)
                Log.e("ButtonActionView", "warning: button icon and button type both declared, ignoring button type");
            buttonImage.setImageResource(buttonIcon);
        } else if(buttonType>-1&&buttonType<ButtonType.values().length) {
            ButtonType type = ButtonType.values()[buttonType];
            buttonImage.setImageResource(getImageForButton(type));
        } else {
            Log.e("ButtonActionView", "no valid icon or button type for action");
        }
        resArray.recycle();
    }
    public int getImageForButton(ButtonType type) {
        switch (type) {
            case SELECT:
                /* Snow2 uses bluetooth remote, use different icon to represent this */
                return (UIUtils.isSnow2()) ? R.drawable.icon_action_select_remote : R.drawable.icon_action_select;
            case BACK:
                return R.drawable.icon_action_back;
            case UP:
                return R.drawable.icon_warning;
            case DOWN:
                return R.drawable.icon_action_down;
            case UPDOWN:
                return R.drawable.icon_action_up_down;
        }
        return R.drawable.icon_warning;
    }
}