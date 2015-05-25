package com.reconinstruments.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Simplified implementation of DialogFragment, used by DialogBuilder
 * should be used by all dialog implementations
 */
public abstract class BaseDialog extends DialogFragment implements DialogBuilder.ViewCallback {

    protected int layout;
    FragmentActivity context;

    boolean isShown = false;
    int dismissTimeout = -1;

    OnKeyListener onKeyListener;

    public BaseDialog(FragmentActivity context,int layout) {
        this.context = context;
        this.layout = layout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layout, container, false);
        updateView(view);
        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (onKeyListener!=null)
                    return onKeyListener.onKey(BaseDialog.this, keyCode, event);
                return false;
            }
        });
        return dialog;
    }

    public void setOnKeyListener(OnKeyListener onKeyListener) {
        this.onKeyListener = onKeyListener;
    }

    public void updateView() {
        updateView(getView());
    }

    public interface OnKeyListener {
        boolean onKey(BaseDialog dialog, int keyCode, KeyEvent event);
    }

    public void show() {
        FragmentManager fm = context.getSupportFragmentManager();
        show(fm,"dialog");
        // create the dialog, allowing DialogFragment.getDialog() and other methods
        // that depend on the state of the dialog lifecycle to return correct values, if needed to finish initializing
        // the dialog elsewhere
        fm.executePendingTransactions();
        isShown = true;

        if(dismissTimeout!=-1) {
            setDismissTimeout(dismissTimeout);
        }
    }

    public void setDismissTimeout(int secondsShown) {
        dismissTimeout = secondsShown;
        if(isShown) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            },dismissTimeout*1000);
        }
    }
}