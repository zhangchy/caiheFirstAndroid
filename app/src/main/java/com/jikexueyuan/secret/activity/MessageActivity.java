package com.jikexueyuan.secret.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jikexueyuan.secret.bean.Comment;
import com.jikexueyuan.secret.bean.Message;
import com.jikexueyuan.secret.common.Config;
import com.jikexueyuan.secret.net.GetComments;
import com.jikexueyuan.secret.net.SendComment;
import com.jikexueyuan.secret.secret.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 13058 on 2016/3/1.
 */
public class MessageActivity extends ListActivity {
    private String msg;
    private String msgId;
    private String phoneNum;
    private String token;
    private TextView tvMessage;
    private MessageActivityAdapter adapter;
    private ListView lvComments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_message);
        adapter = new MessageActivityAdapter(this);
        lvComments = (ListView)findViewById(R.id.select_dialog_listview);
        setListAdapter(adapter);
        Intent intent = getIntent();
        msg = intent.getStringExtra(Config.KEY_MSG);
        msgId = intent.getStringExtra(Config.KEY_MSGID);
        phoneNum = intent.getStringExtra(Config.KEY_PHONE);

        tvMessage = (TextView)findViewById(R.id.tvMessage);

        tvMessage.setText(msg);

        int page = 1;
        int perpage = 10;

        getComments(page,perpage);

        Button btnSendComment = (Button)findViewById(R.id.btnSendComment);
        btnSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText etComment = (EditText)findViewById(R.id.etComment);
                if(TextUtils.isEmpty(etComment.getText().toString())){
                    Toast.makeText(MessageActivity.this, R.string.commentCannotEmpty, Toast.LENGTH_LONG).show();
                    return;
                }
                final ProgressDialog progressDialog = ProgressDialog.show(MessageActivity.this, getResources().getString(R.string.sendingComment), getResources().getString(R.string.sendingCommentPleaseWait));
                new SendComment(MessageActivity.this,phoneNum, token, etComment.getText().toString(), msgId, new SendComment.SuccessCallback() {
                    @Override
                    public void onSuccess(String commentContent) {
                        progressDialog.dismiss();
                        List<Comment> listCurr = new ArrayList<Comment>();
                        listCurr.add(new Comment(phoneNum, commentContent));
                        adapter.addAll(listCurr);
                        etComment.setText(null);
                        etComment.setInputType(InputType.TYPE_NULL); // 关闭软键盘
                    }
                }, new SendComment.FailCallback() {
                    @Override
                    public void onFail(int errorCode) {
                        progressDialog.dismiss();
                        if (errorCode == Config.RESULT_STATUS_INVALID_TOKEN) {
                            /*Toast.makeText(MessageActivity.this, R.string.invalidToken, Toast.LENGTH_LONG).show();
                            return;*/
                            startActivity(new Intent(MessageActivity.this,LoginActivity.class));
                            finish();
                        }
                        Toast.makeText(MessageActivity.this, R.string.sendCommentFail, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void getComments(int page,int perpage){
        final ProgressDialog progressDialog = ProgressDialog.show(MessageActivity.this, getResources().getString(R.string.loadingComments), getResources().getString(R.string.loadingCommentsPleaseWait));
        new GetComments(MessageActivity.this,phoneNum, token, page, perpage, msgId, new GetComments.SuccessCallback() {
            @Override
            public void onSuccess(List<Comment> comments) {
                progressDialog.dismiss();
                adapter.addAll(comments);
            }
        }, new GetComments.FailCallback() {
            @Override
            public void onFail(int errorCode) {
                progressDialog.dismiss();
                if (errorCode == Config.RESULT_STATUS_INVALID_TOKEN) {
                    /*Toast.makeText(MessageActivity.this, R.string.invalidToken, Toast.LENGTH_LONG).show();
                    return;*/
                    startActivity(new Intent(MessageActivity.this,LoginActivity.class));
                    finish();
                }
                Toast.makeText(MessageActivity.this, R.string.getCommentFail, Toast.LENGTH_LONG).show();
            }
        });
    }
}
