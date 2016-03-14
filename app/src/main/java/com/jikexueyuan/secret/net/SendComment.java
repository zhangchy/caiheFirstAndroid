package com.jikexueyuan.secret.net;

import android.content.Context;

import com.jikexueyuan.secret.bean.Comment;
import com.jikexueyuan.secret.common.Config;
import com.jikexueyuan.secret.common.UIHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 13058 on 2016/3/3.
 */
public class SendComment {
    public SendComment(Context context,String phoneNum, String token, final String content, String msgId,final SuccessCallback successCallback,final FailCallback failCallback,final TimeOutCallback timeOutCallback) {
        Map<String,String> params = new HashMap<String,String>();
        params.put(Config.KEY_PHONE_MD5,phoneNum);
        params.put(Config.KEY_TOKEN,token);
        params.put(Config.KEY_MSGID,msgId);
        params.put(Config.KEY_COMMENT_CONTENT,content);
        new NetConnections(context, Config.SERVER_URL + Config.REQUET_URL_PUB_COMMENT, HttpMethod.POST, new NetConnections.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    switch (jsonObject.getInt(Config.RESULT_STATUS)) {
                        case Config.RESULT_STATUS_SUCCESS:
                            if (successCallback != null) {
                                successCallback.onSuccess(content);
                            }
                            break;
                        case Config.RESULT_STATUS_INVALID_TOKEN:
                            if (failCallback != null) {
                                failCallback.onFail(Config.RESULT_STATUS_INVALID_TOKEN);
                            }
                            break;
                        default:
                            if (failCallback != null) {
                                failCallback.onFail(Config.RESULT_STATUS_FAIL);
                            }
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (failCallback != null) {
                        failCallback.onFail(Config.RESULT_STATUS_FAIL);
                    }
                }
            }
        }, new NetConnections.FailCallback() {
            @Override
            public void onFail() {
                if (failCallback != null) {
                    failCallback.onFail(Config.RESULT_STATUS_FAIL);
                }
            }
        }, new NetConnections.TimeOutCallback() {
            @Override
            public void onTimeOut() {
                if (timeOutCallback != null) {
                    timeOutCallback.onTimeOut();
                }
            }
        }, params);
    }

    public static interface SuccessCallback {
        public void onSuccess(String commentContent);
    }

    public static interface FailCallback {
        public void onFail(int errorCode);
    }

    public static interface TimeOutCallback{
        public void onTimeOut();
    }
}
