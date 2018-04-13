package com.example.meiniepan.togetherscrollview.widget;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.SensorManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewConfiguration;

import com.gs.buluo.common.utils.DensityUtils;


/**
 * Created by Solang on 2017/10/31.
 */

public class WalletHeadRecyclerView extends RecyclerView {
    private LinearLayoutManager mLayoutManager;
    private int mItemWidth;
    private OnSelectListener mOnSelectListener;
    private int mSelected = -1;
    private ScrollViewListener scrollViewListener;
    private int sx = 0;
    private boolean isMark = true;

    public WalletHeadRecyclerView(Context context) {
        super(context);
    }

    public WalletHeadRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mItemWidth = CommonUtil.getScreenWidth(getContext()) - DensityUtils.dip2px(getContext(), 60);
        init();
    }

    private void init() {
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        setLayoutManager(mLayoutManager);
        addOnScrollListener(new OnWheelScrollListener());
    }

    public void setOnSelectListener(OnSelectListener listener) {
        mOnSelectListener = listener;
    }

    public void setOnScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }


    @Override
    public boolean fling(int velocityX, int velocityY) {
        //滑动系数
        mPhysicalCoeff = SensorManager.GRAVITY_EARTH   // g (m/s^2)
                * 39.37f               // inch/meter
                * getContext().getResources().getDisplayMetrics().density * 160.0f                 // pixels per inch
                * 0.84f;

        int v;
        int touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        Log.e("=========", "x " + velocityX + " s " + touchSlop);
        if (Math.abs(velocityX) <= 3 * touchSlop) return false;//速率太低直接返回，过滤无效fling
        int firstVisiblePos = mLayoutManager.findFirstVisibleItemPosition();
        if (firstVisiblePos == RecyclerView.NO_POSITION) {
            return false;
        }
        Rect rect = new Rect();
        mLayoutManager.findViewByPosition(firstVisiblePos).getHitRect(rect);
        double n = getSplineFlingDistance(velocityX) / mItemWidth;
        int num = Double.valueOf(n).intValue();
        //把double转化为int,通过整数num求出恰好滑动整数个item的初始速率
        if (velocityX > 0)
            v = Double.valueOf(getVelocityByDistance(num * mItemWidth + Math.abs(rect.right) - DensityUtils.dip2px(getContext(), 20))).intValue();
        else
            v = Double.valueOf(getVelocityByDistance(num * mItemWidth + Math.abs(rect.left) + DensityUtils.dip2px(getContext(), 20))).intValue();
        if (velocityX < 0) v = -v;
        return super.fling(v, velocityY);
    }

    private static final float INFLEXION = 0.35f; // Tension lines cross at (INFLEXION, 1)
    // Fling friction
    private static float mFlingFriction = ViewConfiguration.getScrollFriction();
    private static float mPhysicalCoeff;
    private static float DECELERATION_RATE = (float) (Math.log(0.78) / Math.log(0.9));

    private double getSplineDeceleration(int velocity) {
        return Math.log(INFLEXION * Math.abs(velocity) / (mFlingFriction * mPhysicalCoeff));
    }

    private static double getSplineDecelerationByDistance(double distance) {
        final double decelMinusOne = DECELERATION_RATE - 1.0;
        return decelMinusOne * (Math.log(distance / (mFlingFriction * mPhysicalCoeff))) / DECELERATION_RATE;
    }

    //通过初始速度获取最终滑动距离
    private double getSplineFlingDistance(int velocity) {
        final double l = getSplineDeceleration(velocity);
        final double decelMinusOne = DECELERATION_RATE - 1.0;
        return mFlingFriction * mPhysicalCoeff * Math.exp(DECELERATION_RATE / decelMinusOne * l);
    }

    //通过需要滑动的距离获取初始速度
    public static int getVelocityByDistance(double distance) {
        final double l = getSplineDecelerationByDistance(distance);
        int velocity = (int) (Math.exp(l) * mFlingFriction * mPhysicalCoeff / INFLEXION);
        return Math.abs(velocity);
    }

    //获取滑动的时间
    /* Returns the duration, expressed in milliseconds */
    private int getSplineFlingDuration(int velocity) {
        final double l = getSplineDeceleration(velocity);
        final double decelMinusOne = DECELERATION_RATE - 1.0;
        return (int) (1000.0 * Math.exp(l / decelMinusOne));
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        sx = sx + dx;
        if (scrollViewListener != null && isMark) {//isMark防止无限嵌套滑动
            scrollViewListener.onScrollChanged(this, sx, 0);
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        scrollBy(x - sx, 0);
    }

    public void setmark(boolean b) {
        isMark = b;
    }

    private class OnWheelScrollListener extends OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                int mmSelected;
                //当控件停止滚动时，获取可视范围第一个item的位置，滚动调整控件以使选中的item刚好处于正中间
                int firstVisiblePos = mLayoutManager.findFirstVisibleItemPosition();
                if (firstVisiblePos == RecyclerView.NO_POSITION) {
                    return;
                }
                Rect rect = new Rect();
                mLayoutManager.findViewByPosition(firstVisiblePos).getHitRect(rect);
                Log.e("left1", rect.left + "");
                if (rect.left == 0) return;
                if (Math.abs(rect.left) > mItemWidth / 2) {
                    smoothScrollBy(rect.right - DensityUtils.dip2px(getContext(), 20), 0);
                    mmSelected = firstVisiblePos + 1;
                } else {
                    smoothScrollBy(rect.left - DensityUtils.dip2px(getContext(), 20), 0);
                    mmSelected = firstVisiblePos;
                }
                //回弹结束后的回调
                if (Math.abs(rect.left) == 0 && mOnSelectListener != null && mmSelected != mSelected) {
                    mSelected = mmSelected;
                    mOnSelectListener.onSelect(mSelected);
                }
            }
        }
    }

    public interface OnSelectListener {
        void onSelect(int position);
    }
}
