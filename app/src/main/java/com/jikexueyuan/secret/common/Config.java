package com.jikexueyuan.secret.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 13058 on 2016/2/29.
 */
public class Config {
    public static String KEY_TOKEN="token";
    public static String KEY_PHONE="phone";
    private static String APP_ID = "com.jikexueyuan.secret";
    public static String CHARSET = "utf-8";

    public static String SERVER_URL = "http://10.118.1.48:8080";
    public static String KEY_PHONE_GET_CODE = "PHONE";
    public static String RESULT_STATUS = "status";
    public static final int RESULT_STATUS_SUCCESS = 1;
    public static final int RESULT_STATUS_FAIL = 0;
    public static final int RESULT_STATUS_INVALID_TOKEN = 2;
    public static final long REQUET_TIME = 10000l;

    public static String KEY_PHONE_MD5 = "phone_md5";
    public static String KEY_CODE = "code";
    public static String KEY_CONTACTS = "contacts";
    public static String KEY_PAGE="page";
    public static String KEY_PERPAGE="perpage";
    public static String KEY_MSGS = "items";
    public static String KEY_COMMENT_ITEMS = "items";
    public static String KEY_COMMENT_CONTENT = "content";
    public static String KEY_MSG = "msg";
    public static String KEY_MSGID = "msgId";

    public static String REQUET_URL_SEND_PASS="/android_send_pass";
    public static String REQUET_URL_LOGIN="/android_login";
    public static String REQUET_URL_CONTACTS="/android_upload_contacts";
    public static String REQUET_URL_TIMELINE="/android_timeline";
    public static String REQUET_URL_GET_COMMENTS = "/android_get_comment";
    public static String REQUET_URL_PUB_COMMENT = "/android_pub_comment";
    public static String getCachedToken(Context context){
        return context.getSharedPreferences(APP_ID,Context.MODE_PRIVATE).getString(KEY_TOKEN,null);
    }

    public static void cacheToke(Context context,String token) {
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_TOKEN,token);
        editor.commit();
    }

    public static String getCachedPhone(Context context){
        return context.getSharedPreferences(APP_ID,Context.MODE_PRIVATE).getString(KEY_PHONE,null);
    }

    public static void cachePhone(Context context,String phone){
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_ID,Context.MODE_PRIVATE).edit();
        editor.putString(KEY_PHONE,phone);
        editor.commit();
    }
}
