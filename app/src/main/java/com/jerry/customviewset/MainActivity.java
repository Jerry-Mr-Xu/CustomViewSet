package com.jerry.customviewset;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 该项目收集了一些常用自定义View
 *
 * @author xujierui
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private RecyclerView.LayoutManager rvMainMenuLayoutManager;
    private RecyclerView.Adapter rvMainMenuAdapter;
    private RecyclerView.ItemDecoration rvMainMenuDecoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initViews();
    }

    private void initViews() {
        RecyclerView rvMainMenu = findViewById(R.id.rv_main_menu);
        if (rvMainMenuLayoutManager != null) {
            rvMainMenu.setLayoutManager(rvMainMenuLayoutManager);
        }
        if (rvMainMenuAdapter != null) {
            rvMainMenu.setAdapter(rvMainMenuAdapter);
        }
        if (rvMainMenuDecoration != null) {
            final int decorationCount = rvMainMenu.getItemDecorationCount();
            try {
                for (int i = 0; i < decorationCount; i++) {
                    rvMainMenu.removeItemDecorationAt(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            rvMainMenu.addItemDecoration(rvMainMenuDecoration);
        }

    }

    private void initData() {
        // 获取xml中定义的主菜单列表
        List<String> mainMenuStrList = Arrays.asList(getResources().getStringArray(R.array.main_menu_array));

        rvMainMenuAdapter = new MainMenuAdapter(mainMenuStrList);
        rvMainMenuLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvMainMenuDecoration = new MainMenuAdapter.DividerDecoration(((LinearLayoutManager) rvMainMenuLayoutManager).getOrientation(), Utility.dp2Px(this, 1), ContextCompat.getColor(this, R.color.gray));
    }

    public static class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.ViewHolder> {
        private static final String TAG = "MainMenuAdapter";

        private List<String> dataList;

        public MainMenuAdapter(@NonNull List<String> dataList) {
            setDataList(dataList);
        }

        public void setDataList(@NonNull List<String> dataList) {
            if (this.dataList != null) {
                this.dataList.clear();
            } else {
                this.dataList = new ArrayList<>(dataList.size());
            }
            this.dataList.addAll(dataList);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_main_menu, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            if (i < dataList.size()) {
                viewHolder.tvTitle.setText(dataList.get(i));
            }
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            private static final String TAG = "ViewHolder";

            public TextView tvTitle;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tv_title);
            }
        }

        public static class DividerDecoration extends RecyclerView.ItemDecoration {
            private static final String TAG = "DividerDecoration";
            private int orientation;
            private int dividerLengthInPx;

            private Rect dividerRect = new Rect();
            private Paint dividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

            public DividerDecoration(int orientation, int dividerLengthInPx, int dividerColor) {
                this.orientation = orientation;
                this.dividerLengthInPx = dividerLengthInPx;

                this.dividerPaint.setColor(dividerColor);
                this.dividerPaint.setStyle(Paint.Style.FILL);
                this.dividerPaint.setStrokeWidth(0);
            }

            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.onDraw(c, parent, state);
                switch (orientation) {
                    case LinearLayoutManager.VERTICAL: {
                        drawHorDivider(c, parent);
                        break;
                    }
                    case LinearLayoutManager.HORIZONTAL: {
                        drawVerDivider(c, parent);
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }

            /**
             * 绘制纵向分割线
             *
             * @param canvas 画板
             * @param parent RecyclerView对象
             */
            private void drawVerDivider(Canvas canvas, RecyclerView parent) {
                canvas.save();
                // 根据是否clipPadding来决定分割线的上下纵坐标
                int top;
                int bottom;
                if (parent.getClipToPadding()) {
                    top = parent.getPaddingTop();
                    bottom = parent.getHeight() - parent.getPaddingBottom();
                    canvas.clipRect(parent.getPaddingLeft(), top, parent.getWidth() - parent.getPaddingRight(), bottom);
                } else {
                    top = 0;
                    bottom = parent.getHeight();
                }

                final int childCount = parent.getChildCount();

                for (int i = 0; i < childCount; ++i) {
                    if (i == childCount - 1) {
                        // 如果是最后一个则不画分割线
                        break;
                    }
                    View child = parent.getChildAt(i);
                    parent.getDecoratedBoundsWithMargins(child, this.dividerRect);
                    int right = this.dividerRect.right + Math.round(child.getTranslationY());
                    int left = right - dividerLengthInPx;
                    canvas.drawRect(left, top, right, bottom, this.dividerPaint);
                }

                canvas.restore();
            }

            /**
             * 绘制横向分割线
             *
             * @param canvas 画板
             * @param parent RecyclerView对象
             */
            private void drawHorDivider(Canvas canvas, RecyclerView parent) {
                canvas.save();
                // 根据是否clipPadding来决定分割线的左右横坐标
                int left;
                int right;
                if (parent.getClipToPadding()) {
                    left = parent.getPaddingLeft();
                    right = parent.getWidth() - parent.getPaddingRight();
                    canvas.clipRect(left, parent.getPaddingTop(), right, parent.getHeight() - parent.getPaddingBottom());
                } else {
                    left = 0;
                    right = parent.getWidth();
                }

                final int childCount = parent.getChildCount();

                for (int i = 0; i < childCount; ++i) {
                    if (i == childCount - 1) {
                        // 如果是最后一个则不画分割线
                        break;
                    }
                    View child = parent.getChildAt(i);
                    parent.getDecoratedBoundsWithMargins(child, this.dividerRect);
                    int bottom = this.dividerRect.bottom + Math.round(child.getTranslationY());
                    int top = bottom - dividerLengthInPx;
                    canvas.drawRect(left, top, right, bottom, this.dividerPaint);
                }

                canvas.restore();
            }

            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);

                if (parent.getChildLayoutPosition(view) == state.getItemCount() - 1) {
                    // 如果是最末尾一个Item则不加分割线
                    return;
                }

                switch (orientation) {
                    case LinearLayoutManager.VERTICAL: {
                        outRect.set(0, 0, 0, dividerLengthInPx);
                        break;
                    }
                    case LinearLayoutManager.HORIZONTAL: {
                        outRect.set(0, 0, dividerLengthInPx, 0);
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
        }
    }
}
