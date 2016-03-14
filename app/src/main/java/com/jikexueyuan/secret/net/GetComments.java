package com.jikexueyuan.secret.net;

import android.content.Context;

import com.jikexueyuan.secret.bean.Comment;
import com.jikexueyuan.secret.bean.Message;
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
public class GetComments {
    public GetComments(Context context,String phoneNum, String token, int page, int perpage, String msgId,final SuccessCallback successCallback,final FailCallback failCallback,final TimeOutCallback timeOutCallback) {
        Map<String,String> params = new HashMap<String,String>();
        params.put(Config.KEY_PHONE_MD5,phoneNum);
        params.put(Config.KEY_TOKEN,token);
        params.put(Config.KEY_PAGE,String.valueOf(page));
        params.put(Config.KEY_PERPAGE,String.valueOf(perpage));
        params.put(Config.KEY_MSGID,msgId);
        new NetConnections(context, Config.SERVER_URL + Config.REQUET_URL_GET_COMMENTS, HttpMethod.POST, new NetConnections.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    switch (jsonObject.getInt(Config.RESULT_STATUS)) {
                        case Config.RESULT_STATUS_SUCCESS:
                            if (successCallback != null) {
                                List<Comment> comments = new ArrayList<Comment>();
                                JSONArray array = jsonObject.getJSONArray(Config.KEY_COMMENT_ITEMS);
                                JSONObject item;
                                for (int i = 0; i < array.length(); i++) {
                                    item = array.getJSONObject(i);
                                    comments.add(new Comment(item.getString(Config.KEY_PHONE_MD5), item.getString(Config.KEY_COMMENT_CONTENT)));
                                }
                                successCallback.onSuccess(comments);
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
        public void onSuccess(List<Comment> comments);
    }

    public static interface FailCallback {
        public void onFail(int errorCode);
    }

    public static interface TimeOutCallback{
        public void onTimeOut();
    }
}
