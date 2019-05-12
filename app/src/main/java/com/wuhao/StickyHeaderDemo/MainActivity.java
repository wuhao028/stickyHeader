package com.wuhao.StickyHeaderDemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wuhao.StickyHeaderDemo.model.ItemModel;
import com.wuhao.StickyHeaderDemo.util.UIUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView list;
    private SparseArray recordSp = new SparseArray(0);
    private View header, stickyHeader;
    private int mCurrentfirstVisibleItem = 0;
    float TOTAL_Y_DISTANCE, RATIO, ORIGINAL_BACKGROUND_HEIGHT, BAKCGROUND_RATIO, BOTTOM_LINE_DIFF;
    private ProgressBar progressBar;
    private TextView startText, endText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        list = (ListView) findViewById(R.id.rewardpage_list);
        header = LayoutInflater.from(this).inflate(R.layout.countdown_header, null);
        stickyHeader = findViewById(R.id.sticky_part);
        progressBar = (ProgressBar) header.findViewById(R.id.header_progress_bar);
        startText = (TextView) header.findViewById(R.id.progress_bar_text_start);
        endText = (TextView) header.findViewById(R.id.progress_bar_text_end);

        //"welcome back" move up 20dp
        TOTAL_Y_DISTANCE = UIUtils.convertDpToPixel(20, this);
        //"welcome back" move up 20dp,the countdown box move up 28dp, so the difference is 8dp
        RATIO = 8f / 20f;
        //before the animation, the blue background height is 192dp
        ORIGINAL_BACKGROUND_HEIGHT = UIUtils.convertDpToPixel(192, this);
        //after the animation, the blue background height is 142dp, so the background move up 50dp, the difference compare to TOTAL_Y_DISTANCE is 30dp
        BAKCGROUND_RATIO = 30f / 20f;
        // time to show the buttom blue line of countdown box, should be 262-190 = 72dp ,the box move faster than the getScroll,
        // so the gap is actully smaller, at around 64dp , two bottom line meets.   calculate the absolute position would be a better solution
        BOTTOM_LINE_DIFF = UIUtils.convertDpToPixel(64, this);

        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mCurrentfirstVisibleItem = firstVisibleItem;
                View firstView = view.getChildAt(0);
                if (null != firstView) {
                    ItemRecod itemRecord = (ItemRecod) recordSp.get(firstVisibleItem);
                    if (null == itemRecord) {
                        itemRecord = new ItemRecod();
                    }
                    itemRecord.height = firstView.getHeight();
                    itemRecord.top = firstView.getTop();
                    recordSp.append(firstVisibleItem, itemRecord);
                }

                if (getScrollY() == 0) {
                    findViewById(R.id.sticky_part).setVisibility(View.GONE);
                    progressBar.setAlpha(1);
                    startText.setAlpha(1);
                    endText.setAlpha(1);
                    return;
                }

                if (getScrollY() < TOTAL_Y_DISTANCE) {
                    progressBar.setAlpha((float) (TOTAL_Y_DISTANCE - getScrollY()) / TOTAL_Y_DISTANCE);
                    startText.setAlpha((float) (TOTAL_Y_DISTANCE - getScrollY()) / TOTAL_Y_DISTANCE);
                    endText.setAlpha((float) (TOTAL_Y_DISTANCE - getScrollY()) / TOTAL_Y_DISTANCE);

                    header.findViewById(R.id.header_box_container).scrollTo(0, (int) (RATIO * getScrollY()));
                    RelativeLayout relativeLayout = header.findViewById(R.id.header_text_backgroud);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
                    params.height = (int) (ORIGINAL_BACKGROUND_HEIGHT - BAKCGROUND_RATIO * getScrollY());
                    relativeLayout.setLayoutParams(params);
                    stickyHeader.setVisibility(View.GONE);
                } else {
                    stickyHeader.setVisibility(View.VISIBLE);
                }

                if (getScrollY() > BOTTOM_LINE_DIFF) {
                    stickyHeader.findViewById(R.id.countdown_box).setBackground(MainActivity.this.getDrawable(R.drawable.button_four_line));
                } else {
                    stickyHeader.findViewById(R.id.countdown_box).setBackground(MainActivity.this.getResources().getDrawable(R.drawable.button_countdown_three_line));
                }
            }
        });
        list.setDivider(null);
        list.addHeaderView(header);
        list.setAdapter(new ListAdapter(this, getData()));
    }

    private int getScrollY() {
        int height = 0;
        for (int i = 0; i < mCurrentfirstVisibleItem; i++) {
            ItemRecod itemRecod = (ItemRecod) recordSp.get(i);
            if (itemRecod != null) {
                height += itemRecod.height;
            }
        }
        ItemRecod itemRecod = (ItemRecod) recordSp.get(mCurrentfirstVisibleItem);
        if (null == itemRecod) {
            itemRecod = new ItemRecod();
        }
        return height - itemRecod.top;
    }


    class ItemRecod {
        int height = 0;
        int top = 0;
    }

    private ArrayList<ItemModel> getData() {
        ArrayList<ItemModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ItemModel itemModel = new ItemModel();
            itemModel.points = "item " + i;
            itemModel.timestamp = " ";
            list.add(itemModel);
        }
        return list;
    }
}
