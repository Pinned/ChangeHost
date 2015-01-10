package com.knero.library.widget;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.LinearLayout;

public abstract class FloatView extends LinearLayout {

    protected Context mContext;
    protected WindowManager mWindowManager;
    protected WindowManager.LayoutParams mWMLayoutParams;

    public FloatView(Context context) {
        super(context);
        this.mContext = context;
        this.mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.initView();
    }

    public void setLayoutParams(WindowManager.LayoutParams layoutParams) {
        this.mWMLayoutParams = layoutParams;
    }

    public void showFloatView(WindowManager.LayoutParams layoutParams) {
        if (layoutParams == null) {
            layoutParams = initWindowManagerLaoutParams();
        }
        this.mWMLayoutParams = layoutParams;
        this.mWindowManager.addView(this, layoutParams);

    }

    protected abstract WindowManager.LayoutParams initWindowManagerLaoutParams();

    public void showFloatView() {
        this.showFloatView(this.mWMLayoutParams);
    }

    /**
     * 获取手机屏幕的长和宽
     *
     * @return 返回手机屏幕的长和宽，res[0]是屏幕的宽度，res[1]是屏幕的高度
     */
    protected int[] getScreenSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return new int[]{displayMetrics.widthPixels, displayMetrics.heightPixels};
    }

    /**
     * 视图初始化
     */
    protected abstract void initView();

}
