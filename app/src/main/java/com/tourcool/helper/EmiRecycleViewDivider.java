package com.tourcool.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.frame.library.core.util.SizeUtil;


/**
 * @author :zhoujian
 * @description : 自定义分割线
 * @company :翼迈科技
 * @date 2018年03月16日上午 11:45
 * @Email: 971613168@qq.com
 */

public class EmiRecycleViewDivider extends RecyclerView.ItemDecoration {
    private Paint mPaint;
    private Drawable mDivider;
    private Context mContext;
    private static final String TAG = "EmiRecycleViewDivider";
    /**
     * 分割线的左边距,默认为0px
     */
    private int mMarginLeft = 0;
    /**
     * 分割线的右边距,默认为0px
     */
    private int mMarginRight = 0;
    /**
     * 分割线高度，默认为2px
     */
    private float mDividerHeight = 2;
    /**
     * 列表的方向：LinearLayoutManager.VERTICAL或LinearLayoutManager.HORIZONTAL
     */
    private int mOrientation;
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    /**
     * 默认分割线：高度为2px，颜色为灰色
     *
     * @param context
     * @param orientation 列表方向
     */
    public EmiRecycleViewDivider(Context context, int orientation) {
        if (orientation != LinearLayoutManager.VERTICAL && orientation != LinearLayoutManager.HORIZONTAL) {
            throw new IllegalArgumentException("请输入正确的参数！");
        }
        mContext = context;
        mOrientation = orientation;
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param orientation 列表方向
     * @param drawableId  分割线图片
     */
    public EmiRecycleViewDivider(Context context, int orientation, int drawableId) {
        this(context, orientation);
        mContext = context;
        mDivider = ContextCompat.getDrawable(context, drawableId);
        mDividerHeight = mDivider.getIntrinsicHeight();
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param orientation   列表方向
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     */
    public EmiRecycleViewDivider(Context context, int orientation, int dividerHeight, int dividerColor) {
        this(context, orientation);
        mDividerHeight = dividerHeight;
        mContext = context;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(dividerColor);
        mPaint.setStyle(Paint.Style.FILL);
    }


    /**
     * 获取分割线尺寸
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, 0, 0, (int) mDividerHeight);
    }

    /**
     * 绘制分割线
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    /**
     * 绘制横向 item 分割线
     *
     * @param canvas
     * @param parent
     */
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + layoutParams.bottomMargin;
            final float bottom = (top + mDividerHeight);
            if (mDivider != null) {
                mDivider.setBounds(left + mMarginLeft, top, right - mMarginRight, (int) bottom);
                mDivider.draw(canvas);
            }
            if (mPaint != null) {
                canvas.drawRect(left + mMarginLeft, top, right - mMarginRight, bottom, mPaint);
            }
        }
    }


    /**
     * 绘制纵向 item 分割线
     *
     * @param canvas
     * @param parent
     */
    private void drawVertical(Canvas canvas, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + layoutParams.rightMargin;
            final float right = left + mDividerHeight;
            if (mDivider != null) {
                mDivider.setBounds(left, top, (int) right, bottom);
                mDivider.draw(canvas);
            }
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    /**
     * 设置分割线左边距（dp）
     *
     * @param marginLeftDp
     */
    public EmiRecycleViewDivider setDividerMarginLeft(int marginLeftDp) {
        mMarginLeft = SizeUtil.dp2px(marginLeftDp);
        return this;
    }

    /**
     * 设置下划线高度（dp）
     *
     * @param dividerHightDp
     */
    public EmiRecycleViewDivider setDividerHight(float dividerHightDp) {
        mDividerHeight = SizeUtil.dp2px(dividerHightDp);
        return this;
    }

    /**
     * 设置分割线右边距（dp）
     *
     * @param marginRightDp
     */
    public EmiRecycleViewDivider setDividerMarginRight(int marginRightDp) {
        mMarginRight = SizeUtil.dp2px(marginRightDp);
        return this;
    }


    public EmiRecycleViewDivider setDividerColor(int color) {
        if (mPaint != null) {
            mPaint.setColor(ContextCompat.getColor(mContext, color));
        }

        return this;
    }
}
