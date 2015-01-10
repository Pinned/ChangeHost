package com.knero.library.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.knero.library.widget.FloatItemManager;

public class FloatItemService extends Service {
    public FloatItemService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        FloatItemManager.getInstance().showFloatItemView();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        FloatItemManager.getInstance().hidenFloatView();
        super.onDestroy();
    }
}
