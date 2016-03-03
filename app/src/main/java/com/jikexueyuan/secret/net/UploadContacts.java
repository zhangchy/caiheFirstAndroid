package com.jikexueyuan.secret.net;

import android.widget.Toast;

import com.jikexueyuan.secret.common.Config;
import com.jikexueyuan.secret.secret.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 13058 on 2016/3/2.
 */
public class UploadContacts {
    public UploadContacts(String phoneNum,String token,String contacts, final SuccessCallback successCallback,final FailCallback failCallback){
        Map<String,String> params = new HashMap<String,String>();
        params.put(Config.KEY_PHONE_MD5,phoneNum);
        params.put(Config.KEY_TOKEN,token);
        params.put(Config.KEY_CONTACTS,contacts);
        new NetConnections(Config.SERVER_URL + Config.REQUET_URL_CONTACTS, HttpMethod.POST, new NetConnections.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    switch (jsonObject.getInt(Config.RESULT_STATUS)){
                        case Config.RESULT_STATUS_SUCCESS:
                            if(successCallback!=null){
                                successCallback.onSuccess();
                            }
                            break;
                        case Config.RESULT_STATUS_FAIL:
                            if(failCallback!=null){
                                failCallback.onFail(Config.RESULT_STATUS_INVALID_TOKEN);
                            }
                        default:
                            if(failCallback!=null){
                                failCallback.onFail(Config.RESULT_STATUS_FAIL);
                            }
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new NetConnections.FailCallback() {
            @Override
            public void onFail() {
                if(failCallback!=null){
                    failCallback.onFail(Config.RESULT_STATUS_FAIL);
                }
            }
        },params);
    }
    public static interface SuccessCallback{
        public void onSuccess();
    }

    public static interface FailCallback{
        public void onFail(int errorCode);
    }
}
