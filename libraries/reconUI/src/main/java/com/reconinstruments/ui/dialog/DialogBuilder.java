package com.reconinstruments.ui.dialog;

import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.reconinstruments.ui.R;
import com.reconinstruments.ui.UIUtils;
import com.reconinstruments.ui.carousel.CarouselItem;

import java.util.List;

/**
 * Class containing helper functions for constructing dialogs
 */
public class DialogBuilder {

    private static final String TAG = "DialogBuilder";


    FragmentActivity context;
    protected Integer layout;

    ViewCallback viewCallback;
    BaseDialog.OnKeyListener onKeyListener;
    DialogInterface.OnDismissListener onDismissListener;

    DefaultViewCallback defaultViewCallback;

    /**
     * Default time to display dialog for in seconds, if dialog dismissal timeout set
     */
    public static final int DEFAULT_TIMEOUT = 2;

    int dismissTimeout = -1;

    /**
     * Constructor
     * @param context The containing activity
     */
    public DialogBuilder(FragmentActivity context) {
        this.context = context;
    }

    /**
     * @param layout Set layout resource for dialog box
     * @return Current dialog, for call chaining
     */
    public DialogBuilder setLayout(int layout) {
        this.layout = layout;
        return this;
    }

    /**
     * Sets the primary text displayed in the dialog
     * @return Current dialog, for call chaining
     */
    public DialogBuilder setTitle(String title) {
        if(defaultViewCallback==null) defaultViewCallback = new DefaultViewCallback(context);
        defaultViewCallback.title = title;
        return this;
    }
    /**
     * Sets the secondary text displayed in the dialog
     * @return Current dialog, for call chaining
     */
    public DialogBuilder setSubtitle(String subtitle) {
        if(defaultViewCallback==null) defaultViewCallback = new DefaultViewCallback(context);
        defaultViewCallback.subtitle = subtitle;
        return this;
    }
    /**
     * Sets an icon to be displayed in the dialog
     * @return Current dialog, for call chaining
     */
    public DialogBuilder setIcon(int icon) {
        if(defaultViewCallback==null) defaultViewCallback = new DefaultViewCallback(context);
        defaultViewCallback.icon = icon;
        return this;
    }
    /**
     * Sets the dialog icon to be a checkmark
     * @return Current dialog, for call chaining
     */
    public DialogBuilder setCheckmarkIcon() {
        return setIcon(R.drawable.icon_checkmark);
    }
    /**
     * Sets the dialog icon to be a warning icon
     * @return Current dialog, for call chaining
     */
    public DialogBuilder setWarningIcon() {
        return setIcon(R.drawable.icon_warning);
    }
    /**
     * Shows a progress spinner
     * @return Current dialog, for call chaining
     */
    public DialogBuilder showProgress() {
        if(defaultViewCallback==null) defaultViewCallback = new DefaultViewCallback(context);
        defaultViewCallback.showProgress = true;
        return this;
    }

    /**
     * Allows setting a custom method to initialize the view,
     * cannot be used with other methods that set dialog view parameters, (setTitle, setIcon, etc.)
     * @param viewCallback callback method to be called to bind the view data
     * @return Current dialog, for call chaining
     */
    public DialogBuilder setViewCallback(ViewCallback viewCallback) {
        this.viewCallback = viewCallback;
        return this;
    }

    /**
     * Set listener to be invoked when key pressed while dialog is active
     * @return Current dialog, for call chaining
     */
    public DialogBuilder setOnKeyListener(BaseDialog.OnKeyListener onKeyListener) {
        this.onKeyListener = onKeyListener;
        return this;
    }
    /**
     * Set listener to be invoked when dialog is dismissed
     * @return Current dialog, for call chaining
     */
    public DialogBuilder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    /**
     * Causes the dialog to be automatically dismissed after a 2 second timeout
     * @return Current dialog, for call chaining
     */
    public DialogBuilder setDismissTimeout() {
        return setDismissTimeout(DEFAULT_TIMEOUT);
    }

    /**
     * Causes the dialog to be automatically dismissed after a timeout
     * @return Current dialog, for call chaining
     */
    public DialogBuilder setDismissTimeout(int secondsShown) {
        dismissTimeout = secondsShown;
        return this;
    }

    /**
     * Create a CarouselDialog to select an option from a list of items
     * @param items List of selectable items
     * @return Created dialog, just call dialog.show to show the dialog
     */
    public CarouselDialog createSelectionDialog(List<? extends CarouselItem> items) {
        return createSelectionDialog(items,0,null);
    }
    /**
     * Create a CarouselDialog to select an option from a list of items
     * @param items List of selectable items
     * @param onItemSelectedListener listener to be invoked when an item is selected
     * @return Created dialog, just call dialog.show to show the dialog
     */
    public CarouselDialog createSelectionDialog(List<? extends CarouselItem> items,
                                               CarouselDialog.OnItemSelectedListener onItemSelectedListener) {
        return createSelectionDialog(items,0,onItemSelectedListener);
    }
    /**
     * Create a CarouselDialog to select an option from a list of items
     * @param items List of selectable items
     * @param selectedItem initially selected item
     * @param onItemSelectedListener listener to be invoked when an item is selected
     * @return Created dialog, just call dialog.show to show the dialog
     */
    public CarouselDialog createSelectionDialog(List<? extends CarouselItem> items,int selectedItem,
                                               CarouselDialog.OnItemSelectedListener onItemSelectedListener) {
        preCreateDialog(R.layout.carousel_host_divider);
        CarouselDialog dialog = new CarouselDialog(context,layout,items,selectedItem) {
            @Override
            public void updateView(View view) {
                if(viewCallback!=null) {
                    viewCallback.updateView(view);
                }
            }
        };
        postCreateDialog(dialog);
        if(onItemSelectedListener!=null) {
            dialog.setOnItemSelectedListener(onItemSelectedListener);
        }
        return dialog;
    }

    /**
     * Create a standard dialog
     * @return Created dialog, just call dialog.show to show the dialog
     */
    public BaseDialog createDialog() {
        preCreateDialog(R.layout.dialog_standard);
        BaseDialog dialog = new BaseDialog(context,layout) {
            @Override
            public void updateView(View view) {
                if(viewCallback!=null) {
                    viewCallback.updateView(view);
                }
            }
        };
        postCreateDialog(dialog);
        return dialog;
    }

    private void preCreateDialog(int defaultLayout) {
        if(layout==null)
            layout = defaultLayout;

        if(defaultViewCallback!=null) {
            if(viewCallback!=null)
                Log.e(TAG,"Can't use default dialog fields with custom view callback, ignoring set parameters");
            else
                viewCallback = defaultViewCallback;
        }
    }
    private void postCreateDialog(BaseDialog dialog) {
        if(onKeyListener!=null)
            dialog.setOnKeyListener(onKeyListener);
        if(onDismissListener!=null)
            dialog.getDialog().setOnDismissListener(onDismissListener);
        if(dismissTimeout!=-1)
            dialog.setDismissTimeout(dismissTimeout);
    }

    public interface ViewCallback {
        void updateView(View view);
    }

    public static class DefaultViewCallback implements DialogBuilder.ViewCallback {

        private static final String TAG = "DialogViewCallback";

        public FragmentActivity context;
        public int layout;

        protected String title;
        protected String subtitle;
        protected Integer icon;
        protected boolean showProgress = false;

        public DefaultViewCallback(FragmentActivity context) {
            this.context = context;
        }

        @Override
        public void updateView(View view) {
            UIUtils.setOptionalText(title, view, R.id.title);
            UIUtils.setOptionalText(subtitle, view, R.id.subtitle);
            UIUtils.setOptionalImage(icon, view, R.id.icon);
            View progressView = view.findViewById(R.id.progress_bar);
            if(progressView!=null) {
                progressView.setVisibility(showProgress ? View.VISIBLE : View.GONE);
            }
        }
    }
}
