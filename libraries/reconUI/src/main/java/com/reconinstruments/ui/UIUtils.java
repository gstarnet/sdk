package com.reconinstruments.ui;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class UIUtils {

    public static final int JET_SCREEN_WIDTH = 428;
    public static final int JET_SCREEN_HEIGHT = 240;
    public static final int JET_STATUS_BAR_HEIGHT = 30;
    public static final int JET_WINDOW_HEIGHT = JET_SCREEN_HEIGHT-JET_STATUS_BAR_HEIGHT;

    /**
     * @return True if the current device is Snow2, false if Jet
     */
    static public boolean isSnow2() {
        return Build.MODEL.equalsIgnoreCase("Snow2");
    }

    /**
     * Set the text in a TextView if there is both text and a TextView
     * Having the text content set but no view indicates an error
     * Only use if text is only set once, otherwise save the view to prevent repeat calls to findViewById
     * @param text Optional text content to be displayed in the view
     * @param parent View containing TextView
     * @param textViewId Id of TextView within parent
     */
    public static void setOptionalText(String text, View parent, int textViewId) {
        TextView textView = (TextView)parent.findViewById(textViewId);
        if(text!=null) {
            if(textView!=null) {
                textView.setText(text);
                textView.setVisibility(View.VISIBLE);
            } else {
                Log.e("UIUtils", "text set but no view with text view id found in layout");
            }
        } else if(textView!=null) {
            textView.setVisibility(View.GONE);
        }
    }
    /**
     * Set the image in an ImageView if there is both an image and an ImageView
     * Having an image set but no view indicates an error
     * Only use if image is only set once, otherwise save the view to prevent repeat calls to findViewById
     * @param imageId Optional image resource to be displayed in the view
     * @param parent View containing ImageView
     * @param imageViewId Id of ImageView within parent
     */
    public static void setOptionalImage(Integer imageId, View parent, int imageViewId) {
        ImageView imageView = (ImageView)parent.findViewById(imageViewId);
        if(imageId!=null) {
            if(imageView!=null) {
                imageView.setImageResource(imageId);
                imageView.setVisibility(View.VISIBLE);
            } else {
                Log.e("UIUtils", "image set but no view with image view id found in layout");
            }
        } else if(imageView!=null) {
            imageView.setVisibility(View.GONE);
        }
    }
}
