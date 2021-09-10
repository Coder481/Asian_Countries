package com.hrithik.asiancountries;

import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class MyApplication extends Application {


    private ProgressDialog dialog;

    public void showDialog(Context context, String msg){

        if (dialog != null)
            return;


        dialog = new ProgressDialog(context); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        dialog.setTitle("Loading");
        dialog.setMessage(msg);
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
//
//        dialog = new Dialog(context);
//        dialog.setTitle(msg);
//        dialog.setCancelable(false);
//        dialog.create();
//        dialog.show();
    }

    public void hideDialog(){
        if (dialog != null){
            dialog.dismiss();
        }
    }

    public boolean isOffline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo dataNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return !(wifiNetworkInfo.isConnected() || dataNetworkInfo.isConnected());
    }
}
