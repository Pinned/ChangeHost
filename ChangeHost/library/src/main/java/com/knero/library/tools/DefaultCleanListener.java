package com.knero.library.tools;

/**
 * Created by knero on 1/9/2015.
 */
public class DefaultCleanListener implements DataCleanListener{
    @Override
    public boolean dealByself() {
        return false;
    }

    @Override
    public void onSubmit() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

}
