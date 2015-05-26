package com.reconinstruments.ui.examples.list;

import android.view.View;
import android.widget.TextView;
import com.reconinstruments.ui.examples.R;
import com.reconinstruments.ui.list.SimpleArrayAdapter;
import com.reconinstruments.ui.list.SimpleListActivity;
import com.reconinstruments.ui.list.SimpleListItem;
import com.reconinstruments.ui.list.StandardListItem;

import java.util.Arrays;
import java.util.List;


public class ListItemTypes extends SimpleListActivity {

    public class CustomLayoutItem extends StandardListItem {
        public CustomLayoutItem(String text, String subText) {
            super(R.layout.default_list_item_custom_layout,text,subText,null,null);
        }
    }
    public class CustomItem extends SimpleListItem {
        String text1;
        String text2;
        String text3;
        public CustomItem(String text1,String text2,String text3) {
            this.text1 = text1;
            this.text2 = text2;
            this.text3 = text3;
        }
        @Override
        public int getLayoutId() {
            return R.layout.custom_list_item;
        }
        @Override
        public void updateView(View view) {
            TextView text1TV = (TextView) view.findViewById(R.id.custom_text_1);
            TextView text2TV = (TextView) view.findViewById(R.id.custom_text_2);
            TextView text3TV = (TextView) view.findViewById(R.id.custom_text_3);
            text1TV.setText(text1);
            text2TV.setText(text2);
            text3TV.setText(text3);
        }
    }

    SimpleListItem[] items = {
            new StandardListItem("Default item text only"),
            new StandardListItem("Default w/ icon", R.drawable.selectable_icon_display),
            new StandardListItem("With subtext", "subtext"),
            new StandardListItem("Default w/ info icon", null, null, R.drawable.selectable_icon_display),
            new CustomLayoutItem("Custom layout", "subtext"),
            new CustomItem("Custom view binding", "subtext 1","subtext 2")
    };

    @Override
    public SimpleArrayAdapter createAdapter(List<SimpleListItem> contents) {
        return new SimpleArrayAdapter<SimpleListItem>(this,contents) {
            @Override
            public int getViewTypeCount() {
                return 3;
            }
            @Override
            public int getItemViewType(int position) {
                Class itemClass = getItem(position).getClass();
                if(itemClass==StandardListItem.class)
                    return 0;
                if(itemClass==CustomLayoutItem.class)
                    return 1;
                if(itemClass==CustomItem.class)
                    return 2;
                return -1;
            }
        };
    }
    @Override
    public List<SimpleListItem> createContents() {
        return Arrays.asList(items);
    }
}