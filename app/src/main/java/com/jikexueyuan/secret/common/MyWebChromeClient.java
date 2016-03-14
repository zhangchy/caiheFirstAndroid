package com.jikexueyuan.secret.common;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by 13058 on 2016/3/10.
 */
public class MyWebChromeClient extends WebChromeClient{
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        Log.i("progress",newProgress+";;;;;;");
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        Log.i("title",title+";;;;;;");
    }

    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        Log.i("icon++++++", String.valueOf(icon.describeContents())+"/??????????");
    }

}
