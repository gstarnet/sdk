package com.reconinstruments.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import com.reconinstruments.ui.FontUtils;
import com.reconinstruments.ui.R;

import java.io.*;

public class ReconTextView extends TextView {

    public ReconTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public ReconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ReconTextView(Context context) {
        super(context);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ReconTextView);
        int fontResource = a.getResourceId(R.styleable.ReconTextView_fontResource, -1);
        if(fontResource!=-1) {
            setTypeface(FontUtils.getFontFromRes(getContext(),fontResource));
        }
        a.recycle();
    }
}
