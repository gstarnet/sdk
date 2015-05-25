package com.reconinstruments.ui.breadcrumb;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * Custom toast tied to a parent view, used to display a breadcrumb view without the breadcrumbs living longer
 * than the parent view or interrupting other toasts
 */
public class BreadcrumbToast {

    // duration for fade in, waiting to fade out and fading out, for total animation time of FADE_DURATION*3
    static final int FADE_DURATION = 1000;

    BreadcrumbView breadcrumbView;

    Handler fadeOutHandler;
    ObjectAnimator fadeInAnimator;
    ObjectAnimator fadeOutAnimator;

    public static class FadeOutHandler extends Handler {
        ObjectAnimator animator;
        FadeOutHandler(ObjectAnimator fadeOutAnimator) {
            this.animator = fadeOutAnimator;
        }
        @Override
        public void handleMessage(Message msg) {
            animator.start();
        }
    }

    /**
     * Creates a new breadcrumb toast, but does not display it, use updateBreadcrumbs to show the breadcrumbs
     * at a specific position
     *
     * @param context Activity context
     * @param parent Parent view to attach toast to
     * @param horizontal horizontal breadcrumbs
     * @param size number of items in list
     */
    public BreadcrumbToast(Activity context,ViewGroup parent,boolean horizontal,int size) {
        breadcrumbView = new BreadcrumbView(context,horizontal,0,size);
        ViewGroup.LayoutParams params = horizontal ?
                new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT) :
                new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

        parent.addView(breadcrumbView, params);

        fadeInAnimator = ObjectAnimator.ofFloat(breadcrumbView, "alpha", 0.0f, 1.0f).setDuration(FADE_DURATION);
        fadeInAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                fadeOutHandler.sendEmptyMessageDelayed(0, FADE_DURATION);
            }
        });
        fadeOutAnimator = ObjectAnimator.ofFloat(breadcrumbView, "alpha", 1.0f, 0.0f).setDuration(FADE_DURATION);
        fadeOutHandler = new FadeOutHandler(fadeOutAnimator);
    }

    /**
     * Triggers display of breadcrumbs, updates position in breadcrumbView and triggers animation of the toast
     * depending on the current state of the animation
     * @param newPosition selected item position
     */
    public void updateBreadcrumbs(int newPosition) {
        breadcrumbView.updatePosition(newPosition);
        breadcrumbView.invalidate();

        if(fadeInAnimator.isRunning()) {
            // do nothing, view is already fading in
        } else if(fadeOutAnimator.isRunning()) {
            // view is currently fading out, cancel fade out and trigger new fade in
            // animating from current alpha level back to 1
            fadeOutAnimator.cancel();
            float currentAlpha = breadcrumbView.getAlpha();
            int duration = (int)((1.0-currentAlpha)*FADE_DURATION);
            fadeInAnimator.setFloatValues(currentAlpha, 1.0f);
            fadeInAnimator.setDuration(duration);
            fadeInAnimator.start();
        }
        // no animation running, view is not visible
        else if (breadcrumbView.getAlpha() < 0.01f) {
            // start breadcrumb fade in from 0 alpha
            fadeInAnimator.setFloatValues(0.0f, 1.0f);
            fadeInAnimator.setDuration(FADE_DURATION);
            fadeInAnimator.start();
        }
        // no animation running, view is fully visible
        else if(breadcrumbView.getAlpha() > 0.99f){
            // currently waiting to fade out, delay fade out
            fadeOutHandler.removeMessages(0);
            fadeOutHandler.sendEmptyMessageDelayed(0,FADE_DURATION);
        }
    }
}
