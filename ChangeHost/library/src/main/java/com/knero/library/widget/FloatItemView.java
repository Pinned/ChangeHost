package com.knero.library.widget;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.knero.library.R;

import java.lang.reflect.Field;

/**
 * Created by knero on 1/8/2015.
 */
public class FloatItemView extends FloatView {

    /**
     * 屏幕中心线的位置
     */
    private int middleScreen;

    /**
     * 记录小悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录小悬浮窗的高度
     */
    public static int viewHeight;

    /**
     * 记录系统状态栏的高度
     */
    private static int statusBarHeight;

    /**
     * 记录当前手指位置在屏幕上的横坐标值
     */
    private float xInScreen;

    /**
     * 记录当前手指位置在屏幕上的纵坐标值
     */
    private float yInScreen;

    /**
     * 记录手指按下时在屏幕上的横坐标的值
     */
    private float xDownInScreen;

    /**
     * 记录手指按下时在屏幕上的纵坐标的值
     */
    private float yDownInScreen;

    /**
     * 记录手指按下时在小悬浮窗的View上的横坐标的值
     */
    private float xInView;

    /**
     * 记录手指按下时在小悬浮窗的View上的纵坐标的值
     */
    private float yInView;

    /**
     * 浮动ITEM的点击事件
     */
    private OnFloatItemClickListener floatItemClickListener;
    private View smallLayout;

    public FloatItemView(Context context) {
        super(context);
        middleScreen = mWindowManager.getDefaultDisplay().getWidth() / 2;
    }

    @Override
    protected void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.float_item_view, this);
        this.smallLayout = this.findViewById(R.id.small_window_layout);
        viewWidth = smallLayout.getLayoutParams().width;
        viewHeight = smallLayout.getLayoutParams().height;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
                xInView = event.getX();
                yInView = event.getY();
                xDownInScreen = event.getRawX();
                yDownInScreen = event.getRawY() - getStatusBarHeight();
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                smallLayout.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.item_select_bg));
                break;
            case MotionEvent.ACTION_MOVE:
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                // 手指移动的时候更新小悬浮窗的位置
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                // 如果手指离开屏幕时，xDownInScreen和xInScreen相等，且yDownInScreen和yInScreen相等，则视为触发了单击事件。
                if (xDownInScreen == xInScreen && yDownInScreen == yInScreen) {
                    if (floatItemClickListener != null) {
                        floatItemClickListener.onClick(event);
                    }
                } else {
                    // 如果在左边，则把item靠到左边的位置上去，如果在右边，则把item靠到右边的位置上去
                    if (xInScreen < middleScreen) {
                        xInScreen = 0;
                    } else {
                        xInScreen = middleScreen * 2;
                    }
                    updateViewPosition();
                }
                smallLayout.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.item_unselect_bg));
                break;
            default:
                break;
        }
        return true;
    }

    public void setFloatItemClickListener(OnFloatItemClickListener floatItemClickListener) {
        this.floatItemClickListener = floatItemClickListener;
    }

    /**
     * 更新小悬浮窗在屏幕中的位置。
     */
    private void updateViewPosition() {
        mWMLayoutParams.x = (int) (xInScreen - xInView);
        mWMLayoutParams.y = (int) (yInScreen - yInView);
        mWindowManager.updateViewLayout(this, mWMLayoutParams);
    }

    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    public interface OnFloatItemClickListener {
        void onClick(MotionEvent event);
    }

    @Override
    protected WindowManager.LayoutParams initWindowManagerLaoutParams() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.width = viewWidth;
        layoutParams.height = viewHeight;
        int[] screenSize = getScreenSize();
        layoutParams.x = screenSize[0];
        layoutParams.y = (int) (screenSize[1] * 0.3f);
        return layoutParams;
    }
}
