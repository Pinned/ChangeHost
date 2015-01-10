package com.knero.changehost;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.knero.library.ServiceLocation;
import com.knero.library.tools.ServiceLocationTools;


public class MainActivity extends ActionBarActivity {

    private TextView mShowHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceLocation.DEBUG = false;
        ServiceLocation.startFloat(this, "http://www.baidu.com");
        this.initView();
    }

    private void initView() {
        this.setContentView(R.layout.activity_main);
        this.mShowHost = (TextView) this.findViewById(R.id.show_current_host);

        this.mShowHost.setText(ServiceLocationTools.getInstance().getUrl());
    }

    @Override
    protected void onDestroy() {
        ServiceLocation.stopFloat(this);
        super.onDestroy();
    }
}
