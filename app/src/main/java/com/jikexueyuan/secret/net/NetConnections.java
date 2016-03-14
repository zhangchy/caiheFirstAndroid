package com.jikexueyuan.secret.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.jikexueyuan.secret.common.Config;
import com.jikexueyuan.secret.common.UIHandler;
import com.jikexueyuan.secret.secret.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 网络连接通信类
 * Created by 13058 on 2016/2/29.
 */
public class NetConnections {
    public NetConnections(final Context context,final String url, final HttpMethod method, final SuccessCallback successCallback, final FailCallback failCallback,final TimeOutCallback timeOutCallback, final Map<String, String> kvs) {
        //新建异步类
        final AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                Iterator<Map.Entry<String, String>> iterator = kvs.entrySet().iterator();
                StringBuffer paramStr = new StringBuffer();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = iterator.next();
                    paramStr.append(entry.getKey() + "=" + entry.getValue() + "&");
                }
                if(paramStr.toString().endsWith("&")){
                    paramStr.deleteCharAt(paramStr.toString().length()-1);
                }
                URLConnection uc = null;
                System.out.println("request url:"+url);
                System.out.println("request params:"+paramStr);
                try {
                    switch (method) {
                        case POST:
                            uc = new URL(url).openConnection();
                            uc.setDoOutput(true);
                            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(uc.getOutputStream(), Config.CHARSET));
                            bufferedWriter.write(paramStr.toString());
                            break;
                        default:
                            uc = new URL(url+"?"+paramStr).openConnection();
                            break;
                    }
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(uc.getInputStream(),Config.CHARSET));
                    String line = null;
                    StringBuffer result = new StringBuffer();
                    while ((line = bufferedReader.readLine())!=null){
                        result.append(line);
                    }
                    System.out.println("request result:"+result.toString());
                    return result.toString();
                }catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result){
                super.onPostExecute(result);
                if(result!=null){
                    if(successCallback!=null){
                        successCallback.onSuccess(result);
                    }
                }else{
                    if(failCallback!=null){
                        failCallback.onFail();
                    }
                }
            }
        }.execute();
        new Thread(){
            public void run(){
                try{
                    asyncTask.get(Config.REQUET_TIME, TimeUnit.MILLISECONDS);
                }catch(InterruptedException e){
                    //e.printStackTrace();
                }catch(ExecutionException e){

                }catch(TimeoutException e){ //请求超时处理
                    if(timeOutCallback!=null){
                        timeOutCallback.onTimeOut();
                    }
                }
            }
        }.start();
    }

    public static interface SuccessCallback {
        void onSuccess(String result);
    }

    public static interface FailCallback {
        void onFail();
    }

    public static interface TimeOutCallback{
        void onTimeOut();
    }
}
