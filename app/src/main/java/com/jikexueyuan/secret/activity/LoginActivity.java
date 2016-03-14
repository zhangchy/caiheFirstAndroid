package com.jikexueyuan.secret.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jikexueyuan.secret.bean.User;
import com.jikexueyuan.secret.common.Config;
import com.jikexueyuan.secret.common.MyWebChromeClient;
import com.jikexueyuan.secret.common.MyWebViewClient;
import com.jikexueyuan.secret.common.UIHandler;
import com.jikexueyuan.secret.net.GetCode;
import com.jikexueyuan.secret.net.Login;
import com.jikexueyuan.secret.secret.R;


import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;

/**
 * Created by 13058 on 2016/2/29.
 */
public class LoginActivity extends Activity{
    private Animation rotateLoading;
    private ImageView loading;
    private EditText phone;
    private EditText checkCode;
    private WebView webView;
    public  UIHandler uiHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phone = (EditText)findViewById(R.id.phoneContent);
        checkCode = (EditText)findViewById(R.id.checkCodeContent);
        rotateLoading = AnimationUtils.loadAnimation(LoginActivity.this,R.anim.loading_rolate_animation);
        loading = (ImageView)findViewById(R.id.loading);
        uiHandler = new UIHandler(LoginActivity.this);
        //多语言
        addChangeLanguageButtonListener();

        //获取验证码事件
        addGetCodeButtonListener();
        //添加登陆事件
        addLoginButtonListener();
        addHtml5ButtonListener();
        addLoadingButtonListener();
        addClearLoadingButtonListener();
    }

    //多语言
    private void addChangeLanguageButtonListener(){
        Button languageChoiceButton =(Button)findViewById(R.id.languageChoice);
        languageChoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLocaleLanguage();
            }
        });
    }

    //登陆事件
    private void addLoginButtonListener(){
        Button loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(phone.getText().toString())) {
                    uiHandler.sendMessage(R.string.pleaseFillPhone);
                } else if (TextUtils.isEmpty(checkCode.getText().toString())) {
                    uiHandler.sendMessage(R.string.pleaseFillCheckCode);
                } else {
                    final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, getResources().getString(R.string.logining), getResources().getString(R.string.loginingPleaseWait));
                    new Login(LoginActivity.this, phone.getText().toString(), checkCode.getText().toString(), new Login.SuccessCallback() {
                        @Override
                        public void onSuccess(String token) {
                            progressDialog.dismiss();
                            Config.cachePhone(LoginActivity.this, phone.getText().toString());
                            Config.cacheToke(LoginActivity.this, token);
                            Intent timeLine = new Intent(LoginActivity.this, TimeLineActivity.class);
                            timeLine.putExtra(Config.KEY_PHONE, phone.getText().toString());
                            timeLine.putExtra(Config.KEY_TOKEN, token);
                            startActivity(timeLine);
                        }
                    }, new Login.FailCallback() {
                        @Override
                        public void onFail() {
                            progressDialog.dismiss();
                            Message message = uiHandler.obtainMessage();
                            message.what = UIHandler.DISPLAY_UI_TOAST;
                            message.obj = getString(R.string.checkCodeIsNotCorrect);
                            uiHandler.sendMessage(message);
                        }
                    }, new Login.TimeOutCallback() {
                        @Override
                        public void onTimeOut() {
                            progressDialog.dismiss();
                            Message message = uiHandler.obtainMessage();
                            message.what = UIHandler.DISPLAY_UI_TOAST;
                            message.obj = getString(R.string.connectTimeOut);
                            uiHandler.sendMessage(message);
                        }
                    });
                }
            }
        });
    }

    private void changeLocaleLanguage(){
        //设置语言环境为英文环境
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if(Config.getLocalLanguage(LoginActivity.this).equals(Locale.CHINESE.getLanguage())){
            config.locale = Locale.ENGLISH;
        }else{
            config.locale = Locale.CHINESE;
        }
        Config.setLocalLanguage(LoginActivity.this, config.locale.getLanguage());
        resources.updateConfiguration(config, dm);
        Intent sIntent = this.getIntent();
        this.startActivity(sIntent);
        this.finish();
    }
    //获取验证码
    private void addGetCodeButtonListener(){
        Button getCodeButton =(Button)findViewById(R.id.getCheckCodeButton);
        getCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phone.getText().toString()==null||phone.getText().toString().equals("")){
                    Message message = uiHandler.obtainMessage();
                    message.what = UIHandler.DISPLAY_UI_TOAST;
                    message.obj = getString(R.string.pleaseFillPhone);
                    uiHandler.sendMessage(message);
                    return;
                }
                final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, getResources().getString(R.string.connecting), getResources().getString(R.string.connectingToServerPleaseWait));
                new GetCode(LoginActivity.this, phone.getText().toString(), new GetCode.SuccessCallback() {
                    @Override
                    public void onSuccess(String result) {
                        progressDialog.dismiss();
                        Message message = uiHandler.obtainMessage();
                        message.what = UIHandler.DISPLAY_UI_TOAST;
                        message.obj = getString(R.string.getCheckCodeSucesPleaseCheck);
                        uiHandler.sendMessage(message);
                    }
                }, new GetCode.FailCallback() {
                    @Override
                    public void onFail() {
                        progressDialog.dismiss();
                        Message message = uiHandler.obtainMessage();
                        message.what = UIHandler.DISPLAY_UI_TOAST;
                        message.obj = getString(R.string.getCheckCodeFail);
                        uiHandler.sendMessage(message);
                    }
                }, new GetCode.TimeOutCallback() {
                    @Override
                    public void onTimeOut() {
                        progressDialog.dismiss();
                        Message message = uiHandler.obtainMessage();
                        message.what = UIHandler.DISPLAY_UI_TOAST;
                        message.obj = getString(R.string.connectTimeOut);
                        uiHandler.sendMessage(message);
                    }
                });
            }
        });
    }

    //加载动画
    private void addLoadingButtonListener(){
        Button loadingButton = (Button)findViewById(R.id.loadingButton);
        loadingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.startAnimation(rotateLoading);
            }
        });
    }

    //取消加载动画
    private void addClearLoadingButtonListener(){
        Button clearLoadingButton = (Button)findViewById(R.id.clearLoadingButton);
        clearLoadingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.clearAnimation();
            }
        });
    }

    //加载html5页面
    private void addHtml5ButtonListener(){
        Button showHtmlButton = (Button)findViewById(R.id.showHtmlButton);
        showHtmlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView = (WebView) findViewById(R.id.webView);
                //允许webView中加载js
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new MyWebViewClient(LoginActivity.this, loading, rotateLoading));
                webView.setWebChromeClient(new MyWebChromeClient());
                webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                webView.getSettings().setAllowFileAccess(true);// 设置允许访问文件数据
                webView.loadUrl("file:///android_asset/html5View/index.html");
                webView.addJavascriptInterface(new HtmlView(), "htmlView");
            }
        });
    }


    //控制返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK&&webView.canGoBack()){
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode,event);

    }

    private final class HtmlView {
        @JavascriptInterface
        public void show() {
            //loading.startAnimation(rotateLoading);
            List<User> users = loadUserInfo();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("key", users);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String json = jsonObject.toString();
            webView.loadUrl("javascript:show(" + jsonObject + ")");
            //loading.clearAnimation();
        }

    }

    private List<User> loadUserInfo(){
        List<User> users = null;
        XmlResourceParser xrp = getResources().getXml(R.xml.users);
        try {
            int event = xrp.getEventType();
            User user = null;
            while(event!=XmlResourceParser.END_DOCUMENT){
                switch (event){
                    case XmlResourceParser.START_DOCUMENT:
                        users = new ArrayList<User>();
                        break;
                    case XmlResourceParser.START_TAG:
                        String tagName = xrp.getName();
                        if("user".equals(tagName)){
                            user = new User();
                        }else if (("name").equals(tagName)){
                            String value = xrp.nextText();
                            user.setName(value);
                        }else if(("age").equals(tagName)){
                            String value = xrp.nextText();
                            user.setAge(value);
                        }else if(("sex").equals(tagName)){
                            String value = xrp.nextText();
                            user.setSex(value);
                        }
                        break;
                    case XmlResourceParser.END_TAG:
                        if("user".equals(xrp.getName())){
                            users.add(user);
                            user = null;
                        }
                        break;
                }
                event = xrp.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

}
