package com.jikexueyuan.secret.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.jikexueyuan.secret.activity.LoginActivity;

import java.io.IOException;
import java.io.InputStream;

/**
 * 监听页面各种通知、请求事件
 * Created by 13058 on 2016/3/10.
 */
public class MyWebViewClient extends WebViewClient {
    private Context context;
    private ImageView loading;
    private Animation animation;

    public MyWebViewClient(Context context, ImageView loading, Animation animation) {
        this.context = context;
        this.loading = loading;
        this.animation = animation;
    }


    /**
     * 请求事件：当页面中发生请求路径（出去资源请求）时会通知此函数
     *
     * @param view
     * @param url
     * @return 当返回true时需要自己处理跳转，此时webView中所有的链接都是失效的
     * 当返回false时webView会自己进行跳转
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        /*view.loadUrl(url);
        return true;*/

        return false;//即可

        //启用新的浏览器打开
        /*Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
        return true;*/

    }

    /**
     * 通知事件：当页面加载的时候调用的方法
     *
     * @param view
     * @param url
     * @param favicon
     */
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        loading.startAnimation(animation);
    }

    /**
     * 通知事件：当页面加载结束调用的方法
     *
     * @param view
     * @param url
     */
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        loading.clearAnimation();
    }

    /**
     * 通知事件：当加载页面中的资源时会调用的方法比如一张图片
     * 注：需要调用super。onLoadResource父类的方法，否则报异常
     *
     * @param view
     * @param url
     */
    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
        System.out.println("resource url:" + url);
    }

    /**
     *
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * 通知事件：通知应用程序即将加载的url制定的资源，如果返回本地资源，则不将从网络中获取
     * 注:这个事件发生在非UI线程中
     * @param view
     * @param request
     * @return
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view,
                                                      WebResourceRequest request) {
        /*WebResourceResponse resRes = null;
        Uri uri = request.getUrl();
        String url = uri.getPath();
        if(url!=null&&url.contains("logo")){
            InputStream localCopy = null;
            try {
                localCopy = context.getAssets().open("loading.png");
                resRes = new WebResourceResponse("image/png","UTF-8",localCopy);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resRes;*/
        return null;
    }


    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        //Log.i("http error",error.getErrorCode()+":"+error.getDescription());
        super.onReceivedError(view,request,error);
    }

    @Override
    public void onReceivedHttpError(
            WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        super.onReceivedHttpError(view,request,errorResponse);
    }
}
