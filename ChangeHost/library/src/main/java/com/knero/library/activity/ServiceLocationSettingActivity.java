package com.knero.library.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.knero.library.R;
import com.knero.library.ServiceLocation;
import com.knero.library.tools.DataCleanListener;
import com.knero.library.tools.DataCleanManager;
import com.knero.library.tools.ServiceLocationTools;


/**
 * Created by knero on 1/8/2015.
 */
public class ServiceLocationSettingActivity extends Activity {


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ServiceLocationSettingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    private TextView mHostShow;
    private Spinner mHostSpinner;
    private EditText mHostInput;
    private String mCurrentHost;

    private int mCurrentPort;
    private TextView mPortShow;
    private TextView mPortInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initData();
        this.initView();
    }

    private void initData() {
        mCurrentHost = ServiceLocationTools.getInstance().getHost();
        mCurrentPort = ServiceLocationTools.getInstance().getPort();
    }

    private void initView() {
        this.setContentView(R.layout.setting_service_location);

        this.mHostShow = (TextView) this.findViewById(R.id.host);
        this.mHostInput = (EditText) this.findViewById(R.id.host_input);
        this.mHostSpinner = (Spinner) this.findViewById(R.id.host_spinner);

        this.mPortShow = (TextView) this.findViewById(R.id.port);
        this.mPortInput = (TextView) this.findViewById(R.id.port_input);

        if (!TextUtils.isEmpty(mCurrentHost)) {
            this.mHostShow.setText(mCurrentHost);
        }
        if (mCurrentPort > 0) {
            this.mPortShow.setText(String.valueOf(mCurrentPort));
        } else {
            this.mPortShow.setText("无端口号");
        }
    }

    private void notChanged() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Warm")
                .setMessage("你没有修改任何信息，无需保存")
                .setNegativeButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        alertDialog.show();
    }

    public void submit(View view) {
        final String host = mHostInput.getText().toString();
        final String port = mPortInput.getText().toString();
        if (!TextUtils.isEmpty(host)) {
            if (host.equals(mCurrentHost)) {
                // 提示没有修改
                notChanged();
            } else {
                // 提示要清除数据,并重启应用
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("修改Host地址")
                        .setMessage("修改将会清除你所有的个人数据")
                        .setNegativeButton("确认", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ServiceLocationTools.getInstance().setDefaultLocation(host, port);
                                exitProgram();
                            }

                        })
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                alertDialog.show();
            }
        }

    }

    private void exitProgram() {
        DataCleanListener listener = ServiceLocation.getDataCleanListener();
        DataCleanManager.cleanApplicationData(this);
        if (listener.dealByself()) {
            finish();
        } else {
            listener.onSubmit();
        }
    }


}
