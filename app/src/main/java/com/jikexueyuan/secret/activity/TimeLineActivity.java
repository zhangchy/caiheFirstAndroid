package com.jikexueyuan.secret.activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.jikexueyuan.secret.bean.Message;
import com.jikexueyuan.secret.common.Config;
import com.jikexueyuan.secret.common.UIHandler;
import com.jikexueyuan.secret.localdata.MyContacts;
import com.jikexueyuan.secret.net.TimeLine;
import com.jikexueyuan.secret.net.UploadContacts;
import com.jikexueyuan.secret.secret.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by 13058 on 2016/2/29.
 */
public class TimeLineActivity extends ListActivity {
    private TimeLineActivityAdapter adapter;
    private String phoneNum;
    private String token;
    public UIHandler uiHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        adapter = new TimeLineActivityAdapter(TimeLineActivity.this);
        setListAdapter(adapter);
        phoneNum = getIntent().getStringExtra(Config.KEY_PHONE);
        token = getIntent().getStringExtra(Config.KEY_TOKEN);
        uiHandler = new UIHandler(TimeLineActivity.this);
        uploadContacts();
    }

    private void uploadContacts(){
        final ProgressDialog progressDialog = ProgressDialog.show(TimeLineActivity.this, getResources().getString(R.string.uploadContacts), getResources().getString(R.string.uploadContactsPleaseWait));
        new UploadContacts(TimeLineActivity.this, phoneNum, token, MyContacts.getContactsJSONString(TimeLineActivity.this), new UploadContacts.SuccessCallback() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                android.os.Message message = uiHandler.obtainMessage();
                message.what = UIHandler.DISPLAY_UI_TOAST;
                message.obj = getString(R.string.uploadContactsComplete);
                uiHandler.sendMessage(message);
                loadMessages();
            }
        }, new UploadContacts.FailCallback() {
            @Override
            public void onFail(int errorCode) {
                if (errorCode == Config.RESULT_STATUS_INVALID_TOKEN) {
                    startActivity(new Intent(TimeLineActivity.this, LoginActivity.class));
                    finish();
                    return;
                }
                progressDialog.dismiss();
                android.os.Message message = uiHandler.obtainMessage();
                message.what = UIHandler.DISPLAY_UI_TOAST;
                message.obj =getString( R.string.uploadContactsFail);
                uiHandler.sendMessage(message);
                loadMessages();
            }
        }, new UploadContacts.TimeOutCallback() {
            @Override
            public void onTimeOut() {
                progressDialog.dismiss();
                android.os.Message message = uiHandler.obtainMessage();
                message.what = UIHandler.DISPLAY_UI_TOAST;
                message.obj = getString(R.string.connectTimeOut);
                uiHandler.sendMessage(message);
            }
        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l,v,position,id);
        Message message = adapter.getItem(position);
        Intent intent = new Intent(this,MessageActivity.class);
        intent.putExtra(Config.KEY_MSG,message.getMsg());
        intent.putExtra(Config.KEY_MSGID,message.getMsgId());
        intent.putExtra(Config.KEY_TOKEN,token);
        intent.putExtra(Config.KEY_PHONE, phoneNum);
        startActivity(intent);
    }

    private void loadMessages() {
        int page = 1;
        int perpage = 10;
        String phoneNum = getIntent().getStringExtra(Config.KEY_PHONE);
        String token = getIntent().getStringExtra(Config.KEY_TOKEN);
        final ProgressDialog progressDialog = ProgressDialog.show(TimeLineActivity.this, getResources().getString(R.string.loadingMessage), getResources().getString(R.string.loadingMessagePleaseWait));
        new TimeLine(TimeLineActivity.this, phoneNum, token, page, perpage, new TimeLine.SuccessCallback() {
            @Override
            public void onSuccess(List<Message> messages) {
                progressDialog.dismiss();
                adapter.addAll(messages);
            }
        }, new TimeLine.FailCallback() {
            @Override
            public void onFail(int errorCode) {
                progressDialog.dismiss();
                if (errorCode == Config.RESULT_STATUS_INVALID_TOKEN) {
                    startActivity(new Intent(TimeLineActivity.this, LoginActivity.class));
                    finish();
                }
                android.os.Message message = uiHandler.obtainMessage();
                message.what = UIHandler.DISPLAY_UI_TOAST;
                message.obj = getString(R.string.getMessagesFail);
                uiHandler.sendMessage(message);
            }
        }, new TimeLine.TimeOutCallback() {
            @Override
            public void onTimeOut() {
                progressDialog.dismiss();
                android.os.Message message = uiHandler.obtainMessage();
                message.what = UIHandler.DISPLAY_UI_TOAST;
                message.obj = getString(R.string.connectTimeOut);
                uiHandler.sendMessage(message);
            }
        });
    }
}
