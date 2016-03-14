package com.jikexueyuan.secret.common;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

/**
 * Created by 13058 on 2016/3/10.
 */
public class UIHandler extends Handler {
    public static final int DISPLAY_UI_TOAST = 0;
    public static final int DISPLAY_UI_DIALOG = 1;
    private Context context;
    private ProgressDialog progressDialog;

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public void setProgressDialog(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    public UIHandler(Context context){
        super();
        this.context = context;
    }

    public UIHandler(Context context,ProgressDialog progressDialog){
        super();
        this.context = context;
        this.progressDialog = progressDialog;
    }

    public void sendMessage(int msg){
        Message msgO = this.obtainMessage(UIHandler.DISPLAY_UI_TOAST);
        msgO.obj = msg;
        this.sendMessage(msgO);
    }

    @Override
    public void handleMessage(Message msg){
        switch (msg.what){
            case UIHandler.DISPLAY_UI_TOAST:
                if(Config.getLooper(context)){
                    Toast.makeText(context,String.valueOf(msg.obj),Toast.LENGTH_LONG).show();
                }else{
                    //Looper.prepare();
                    //Config.setLooper(context,true);
                    Toast.makeText(context,String.valueOf(msg.obj),Toast.LENGTH_LONG).show();
                    //Looper.loop();
                }
                break;
            default:
                break;
        }
    }
}
