package com.jikexueyuan.secret.localdata;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.jikexueyuan.secret.common.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 13058 on 2016/2/29.
 */
public class MyContacts {
    public static String getContactsJSONString(Context context){
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        String phoneNum;
        JSONArray phoneJsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        int index = 0;
        while(cursor.moveToNext()){
            phoneNum = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if(phoneNum.startsWith("+86")){
                phoneNum = phoneNum.substring(3);
            }
            try {
                jsonObject.put(Config.KEY_PHONE_MD5,phoneNum);
                phoneJsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println("phoneJsonArray:"+phoneJsonArray);
        }
        return phoneJsonArray.toString();
    }
}
