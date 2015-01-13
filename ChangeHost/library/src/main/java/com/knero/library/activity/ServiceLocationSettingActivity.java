package com.knero.library.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.knero.library.R;
import com.knero.library.ServiceLocation;
import com.knero.library.tools.DataCleanListener;
import com.knero.library.tools.DataCleanManager;
import com.knero.library.tools.ServiceLocationTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


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

    private List<String> mUrls;

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
        mUrls = new ArrayList<String>();
        mUrls.addAll(ServiceLocationTools.getInstance().getHistoryUrls());
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
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mUrls);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mHostSpinner.setAdapter(adapter);
        this.mHostSpinner.setSelection(mUrls.indexOf(ServiceLocationTools.getInstance().getUrl()), true);
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
        if (mHostSpinner.getSelectedItem().toString().equals(
                ServiceLocationTools.getInstance().getUrl()
        )) {
            final String host = mHostInput.getText().toString();
            final String port = mPortInput.getText().toString();

            if (!TextUtils.isEmpty(host)) {
                if (parserUrl(host, port).equals(
                        parserUrl(mCurrentHost,String.valueOf(mCurrentPort)))) {
                    // 提示没有修改
                    notChanged();
                } else {
                    changed(host, port);
                }
            }
        } else {
            changed(mHostSpinner.getSelectedItem().toString(), "");
        }
    }
    private String parserUrl(String host, String port) {
        StringBuffer sb = new StringBuffer();
        if (host.startsWith("http")) {
            sb.append(host);
        } else {
            sb.append("http://" + host);
        }
        if (!TextUtils.isEmpty(port)) {
            sb.append(":");
            sb.append(port);
        }
        return sb.toString();
    }
    private void changed(final String changeHost, final String changePort) {
        // 提示要清除数据,并重启应用
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("修改Host地址")
                .setMessage("修改将会清除你所有的个人数据")
                .setNegativeButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ServiceLocationTools.getInstance().setDefaultLocation(changeHost, changePort);
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
