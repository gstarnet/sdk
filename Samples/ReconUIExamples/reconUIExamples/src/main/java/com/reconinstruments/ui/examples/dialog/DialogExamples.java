package com.reconinstruments.ui.examples.dialog;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.reconinstruments.ui.carousel.CarouselItem;
import com.reconinstruments.ui.carousel.StandardCarouselItem;
import com.reconinstruments.ui.dialog.BaseDialog;
import com.reconinstruments.ui.dialog.CarouselDialog;
import com.reconinstruments.ui.dialog.DialogBuilder;
import com.reconinstruments.ui.examples.R;
import com.reconinstruments.ui.list.SimpleListActivity;
import com.reconinstruments.ui.list.SimpleListItem;
import com.reconinstruments.ui.list.StandardListItem;

import java.util.Arrays;
import java.util.List;


public class DialogExamples extends SimpleListActivity {

    public class ListItem extends StandardListItem {
        String subtitle;
        OnClickCallback callback;
        public ListItem(String text, OnClickCallback callback){
            super(text);
            this.callback = callback;
        }
        public void onClick() {
            callback.onClick(this);
        }
        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
            TextView subtitleView = (TextView)getView().findViewById(R.id.subtitle);
            subtitleView.setVisibility(View.VISIBLE);
            subtitleView.setText(subtitle);
        }
        public String getSubtitle() {
            return subtitle;
        }
    }
    public interface OnClickCallback {
        public void onClick(ListItem item);
    }

    SimpleListItem[] items = {
            new ListItem("Selection Dialog",new OnClickCallback() {
                public void onClick(ListItem item) {
                    createSelectionDialog(item);
                }
            }),
            new ListItem("Pop up Dialog",new OnClickCallback() {
                public void onClick(ListItem item) {
                    createPopupDialog();
                }
            }),
            new ListItem("Basic Dialog",new OnClickCallback() {
                public void onClick(ListItem item) {
                    createBasicDialog();
                }
            }),
            new ListItem("Progress Dialog",new OnClickCallback() {
                public void onClick(ListItem item) {
                    createProgressDialog();
                }
            }),
            new ListItem("Custom Selection Dialog",new OnClickCallback() {
                public void onClick(ListItem item) {
                    createCustomSelectionDialog(item);
                }
            })
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public List<SimpleListItem> createContents() {
        return Arrays.asList(items);
    }

    CarouselItem[] selections = {
            new CheckedSelectionItem("5 mins"),
            new CheckedSelectionItem("10 mins"),
            new CheckedSelectionItem("15 mins")
    };
    public static class CheckedSelectionItem extends StandardCarouselItem {
        public CheckedSelectionItem(String title) {
            super(title);
        }
        @Override
        public int getLayoutId() {
            return R.layout.carousel_item_hidden_checkmark;
        }
    }

    public int timeSelection = 0;

    public void createSelectionDialog(final ListItem listItem) {

        DialogBuilder builder = new DialogBuilder(this).setTitle("Timeout");
        builder.createSelectionDialog(Arrays.asList(selections), timeSelection, new CarouselDialog.OnItemSelectedListener() {
            @Override
            public void onItemSelected(CarouselDialog dialog, CarouselItem item, int position) {
                listItem.setSubtitle(((StandardCarouselItem) item).getTitle());
                timeSelection = position;
                //getAdapter().getPosition(item); ?
                dialog.dismiss();
            }
        }).show();
    }

    private void createPopupDialog() {
        new DialogBuilder(this).setTitle("Warning").setSubtitle("Showing for 2 seconds").setWarningIcon().setDismissTimeout().createDialog().show();
    }

    private void createBasicDialog() {
        new DialogBuilder(this).setTitle("DIALOG").setSubtitle("subtitle").setLayout(R.layout.action_bar_dialog).createDialog().show();
    }

    private void createProgressDialog() {
        new DialogBuilder(this).setTitle("Loading").setSubtitle("(press select to finish)").showProgress().setOnKeyListener(new BaseDialog.OnKeyListener() {
            @Override
            public boolean onKey(BaseDialog dialog, int keyCode, KeyEvent event) {
                if (event.getAction()==KeyEvent.ACTION_UP&&keyCode==KeyEvent.KEYCODE_DPAD_CENTER) {
                    ImageView icon = (ImageView)dialog.getView().findViewById(R.id.icon);
                    icon.setImageResource(R.drawable.icon_checkmark);
                    icon.setVisibility(View.VISIBLE);
                    dialog.getView().findViewById(R.id.progress_bar).setVisibility(View.GONE);
                    dialog.setDismissTimeout(2);
                    return true;
                }
                return false;
            }
        }).createDialog().show();
    }


    int optionSelected = 0;
    public void createCustomSelectionDialog(final ListItem listItem) {
        new CarouselDialogExample(this,optionSelected,new CarouselDialog.OnItemSelectedListener() {
            @Override
            public void onItemSelected(CarouselDialog dialog, CarouselItem item, int position) {
                optionSelected = position;
                listItem.setSubtitle("#"+(position+1));
                dialog.dismiss();
            }
        }).show();
    }
}