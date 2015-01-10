package com.knero.library.widget;

import android.content.Context;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.knero.library.activity.ServiceLocationSettingActivity;

/**
 * Created by knero on 1/8/2015.
 */
public class FloatItemManager {

    private static FloatItemManager instance;

    public static void init(Context context) {
        instance = new FloatItemManager(context);
    }

    public static FloatItemManager getInstance() {
        return instance;
    }

    private Context mContext;

    private WindowManager mWindowManager;
    private FloatItemView floatItemView = null;

    public FloatItemManager(Context context) {
        this.mContext = context;
    }

    public void showFloatItemView() {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE);
        }
        clearAllViews();
        if (floatItemView == null) {
            floatItemView = new FloatItemView(mContext);
            floatItemView.setFloatItemClickListener(new FloatItemView.OnFloatItemClickListener() {
                @Override
                public void onClick(MotionEvent event) {
                    showSettingLocationView();
                }
            });
        }
        floatItemView.showFloatView();
    }

    public void hidenFloatView() {
        clearAllViews();
    }

    private void showSettingLocationView() {
        ServiceLocationSettingActivity.startActivity(mContext);
    }

    /**
     * 清除屏幕上的所有的浮动View
     */
    private void clearAllViews() {
        // 如果WindowManager为空，则不需清除浮动的视图
        if (mWindowManager != null) {
            if (floatItemView != null) {
                mWindowManager.removeView(floatItemView);
                floatItemView = null;
            }
        }
    }

}
