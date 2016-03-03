package com.jikexueyuan.secret.net;

import android.util.Log;

import com.jikexueyuan.secret.common.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 13058 on 2016/3/2.
 */
public class GetCode {
    public GetCode(String phone,final SuccessCallback successCallback,final FailCallback failCallback){
        Map<String,String> params = new HashMap<String,String>();
        params.put(Config.KEY_PHONE_GET_CODE,phone);
        new NetConnections(Config.SERVER_URL+Config.REQUET_URL_SEND_PASS,HttpMethod.POST,new NetConnections.SuccessCallback(){
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    switch (jsonObject.getInt(Config.RESULT_STATUS)){
                        case Config.RESULT_STATUS_SUCCESS:
                            if(successCallback!=null){
                                successCallback.onSuccess(result);
                            }
                            break;
                        default:
                            if(failCallback!=null){
                                failCallback.onFail();
                            }
                            break;
                    }
                } catch (JSONException e) {
                    if(failCallback!=null){
                        failCallback.onFail();
                    }
                    e.printStackTrace();
                }

            }
        },new NetConnections.FailCallback(){
            @Override
            public void onFail() {
                if(failCallback!=null){
                    failCallback.onFail();
                }
            }
        },params);
    }

    public static interface SuccessCallback{
        void onSuccess(String result);
    }
    public static interface FailCallback{
        void onFail();
    }
}