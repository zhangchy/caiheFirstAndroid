package com.jikexueyuan.secret.net;

import android.content.Context;

import com.jikexueyuan.secret.common.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 13058 on 2016/3/2.
 */
public class Login {
    public Login(Context context,String phoneMD5,String code, final SuccessCallback successCallback, final FailCallback failCallback){
        Map<String,String> params = new HashMap<String,String>();
        params.put(Config.KEY_PHONE_MD5,phoneMD5);
        params.put(Config.KEY_CODE,code);
        new NetConnections(context,Config.SERVER_URL+Config.REQUET_URL_LOGIN,HttpMethod.POST,new NetConnections.SuccessCallback(){
            @Override
            public void onSuccess(String result){
                try{
                    JSONObject jsonObject = new JSONObject(result);
                    switch (jsonObject.getInt("status")){
                        case Config.RESULT_STATUS_SUCCESS:
                            if(successCallback!=null){
                                successCallback.onSuccess(jsonObject.getString("token"));
                            }
                            break;
                        default:
                            if(failCallback!=null){
                                failCallback.onFail();
                            }
                            break;
                    }
                }catch (JSONException e) {
                    if(failCallback!=null){
                        failCallback.onFail();
                    }
                    e.printStackTrace();
                }
            }
        },new NetConnections.FailCallback(){
            @Override
            public void onFail(){
                if(failCallback!=null){
                    failCallback.onFail();
                }
            }
        },params);
    }

    public static interface SuccessCallback{
        public void onSuccess(String result);
    }
    public static interface FailCallback{
        public void onFail();
    }
}
