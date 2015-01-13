package com.knero.library.tools;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by knero on 1/8/2015.
 */
public class ServiceLocationTools {

    private static ServiceLocationTools instance;

    public static void init(Context context, String defaultHost) {
        init(context, defaultHost, "");
    }

    public static void init(Context context, String defaultHost, String defaultPort) {
        instance = new ServiceLocationTools(context, defaultHost, defaultPort);
    }

    public static ServiceLocationTools getInstance() {
        return instance;
    }

    private final String HOST = "host";
    private final String PORT = "port";
    public String currentHost;
    public String currentPort;
    private String fileLocation;
    private String historyFiles;

    private Set<String> urls;

    public ServiceLocationTools(Context context, String defaultHost, String defaultPort) {
        try {
            String packageName = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).packageName;
            fileLocation = "/data/data/" + packageName + "/location.properties";
            historyFiles = "/data/data/" + packageName + "/allurl.txt";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Properties props = loadProperties();
        currentHost = props.getProperty(HOST, defaultHost);
        currentPort = props.getProperty(PORT, defaultPort);
        urls = new HashSet<String>();
        try {
            Scanner scanner = new Scanner(new File(historyFiles));
            while (scanner.hasNext()) {
                urls.add(scanner.nextLine());
            }
        } catch (Exception e) {
        }
        saveToHistory();
    }

    public void setDefaultHost(String host) {
        this.currentHost = host;
        storeProperties();
    }

    public void setDefaultPort(int port) {
        this.currentPort = String.valueOf(port);
        storeProperties();
    }

    public void setDefaultLocation(String host, String port) {
        this.currentHost = host;
        if (!TextUtils.isEmpty(port)) {
            this.currentPort = port;
        } else {
            this.currentPort = "0";
        }
        storeProperties();
    }

    private void storeProperties() {
        Properties properties = loadProperties();
        properties.put(PORT, currentPort);
        properties.put(HOST, currentHost);
        try {
            File file = new File(fileLocation);
            if (!file.exists()) {
                file.createNewFile();
            }
            OutputStream os = new FileOutputStream(file);
            properties.store(os, "Copyright (c) Luozc");
            os.flush();
            os.close();
            Log.d("TAG", loadProperties().getProperty(HOST, ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        saveToHistory();
    }

    private void saveToHistory() {
        urls.add(getUrl());
        try {
            FileWriter fw = new FileWriter(new File(historyFiles));
            for (String url : urls) {
                fw.write(url);
                fw.write("\r\n");
            }
            fw.flush();
            fw.close();
        }catch (Exception e) {

        }
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        try {
            File file = new File(fileLocation);
            Log.d("TAG", file.getPath());
            if (!file.exists()) {
                file.createNewFile();
            }
            InputStream is = new FileInputStream(file);
            props.load(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return props;
    }

    public String getHost() {
        if (this.currentHost.startsWith("http")) {
            return this.currentHost;
        } else {
            return String.format("http://%s",this.currentHost);
        }
    }

    public int getPort() {
        if (TextUtils.isEmpty(this.currentPort)) {
            return 0;
        } else {
            return Integer.parseInt(this.currentPort);
        }
    }

    public String getUrl() {
        int port = getPort();
        StringBuffer sb = new StringBuffer(getHost());
        if (port > 0) {
            sb.append(":");
            sb.append(port);
        }
        return sb.toString();
    }

    public Set<String> getHistoryUrls() {
        return urls;
    }

}
