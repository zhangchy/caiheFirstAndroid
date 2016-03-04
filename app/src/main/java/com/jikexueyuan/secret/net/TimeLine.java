package com.jikexueyuan.secret.net;

import android.content.Context;

import com.jikexueyuan.secret.bean.Message;
import com.jikexueyuan.secret.common.Config;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 13058 on 2016/3/2.
 */
public class TimeLine {
    public TimeLine(Context context,String phoneNum,String token,int page,int perpage, final SuccessCallback successCallback, final FailCallback failCallback){
        Map<String,String> params = new HashMap<String,String>();
        params.put(Config.KEY_PHONE_MD5,phoneNum);
        params.put(Config.KEY_TOKEN,token);
        params.put(Config.KEY_PAGE,String.valueOf(page));
        params.put(Config.KEY_PERPAGE,String.valueOf(perpage));
        new NetConnections(context,Config.SERVER_URL + Config.REQUET_URL_TIMELINE, HttpMethod.POST, new NetConnections.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    switch (jsonObject.getInt(Config.RESULT_STATUS)){
                        case Config.RESULT_STATUS_SUCCESS:
                            if(successCallback!=null){
                                List<Message> messages = new ArrayList<Message>();
                                JSONArray array = jsonObject.getJSONArray(Config.KEY_MSGS);
                                JSONObject item;
                                for(int i = 0;i<array.length();i++){
                                    item = array.getJSONObject(i);
                                    messages.add(new Message(item.getString(Config.KEY_PHONE_MD5),item.getString(Config.KEY_MSG),item.getString(Config.KEY_MSGID)));
                                }
                                successCallback.onSuccess(messages);
                            }
                            break;
                        case Config.RESULT_STATUS_INVALID_TOKEN:
                            if(failCallback!=null){
                                failCallback.onFail(Config.RESULT_STATUS_INVALID_TOKEN);
                            }
                            break;
                        default:
                            if(failCallback!=null){
                                failCallback.onFail(Config.RESULT_STATUS_FAIL);
                            }
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if(failCallback!=null){
                        failCallback.onFail(Config.RESULT_STATUS_FAIL);
                    }
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
    public static  interface SuccessCallback{
        public void onSuccess(List<Message> messages);
    }

    public static interface FailCallback{
        public void onFail(int errorCode);
    }
}
