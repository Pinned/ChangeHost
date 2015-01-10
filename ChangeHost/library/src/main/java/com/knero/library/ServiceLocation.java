package com.knero.library;

import android.content.Context;
import android.content.Intent;

import com.knero.library.services.FloatItemService;
import com.knero.library.tools.DataCleanListener;
import com.knero.library.tools.DefaultCleanListener;
import com.knero.library.tools.ServiceLocationTools;
import com.knero.library.widget.FloatItemManager;


/**
 * Created by knero on 1/8/2015.
 */
public class ServiceLocation {

    public static Intent floatIntent;
    private static DataCleanListener dataCleanListener = null;

    public static void startFloat(Context context) {
        startFloat(context, "http://10.0.0.2");
    }

    public static void startFloat(Context context, String defaultHost) {
        ServiceLocationTools.init(context, defaultHost);
        FloatItemManager.init(context);
        if (floatIntent == null) {
            floatIntent = new Intent(context, FloatItemService.class);
        } // end if
        context.startService(floatIntent);
    }


    public static void stopFloat(Context context) {
        if (floatIntent != null) {
            context.stopService(floatIntent);
            floatIntent = null;
        } // end if
    }

    public static void setDataCleanListener(DataCleanListener listener) {
        dataCleanListener = listener;
    }

    public static DataCleanListener getDataCleanListener() {
        if (dataCleanListener == null) {
            dataCleanListener = new DefaultCleanListener();
        } // end if
        return dataCleanListener;
    }

}
