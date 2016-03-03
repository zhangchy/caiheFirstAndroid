package com.jikexueyuan.secret.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jikexueyuan.secret.common.Config;
import com.jikexueyuan.secret.net.GetCode;
import com.jikexueyuan.secret.net.Login;
import com.jikexueyuan.secret.secret.R;

/**
 * Created by 13058 on 2016/2/29.
 */
public class LoginActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText phone = (EditText)findViewById(R.id.phoneContent);
        final EditText checkCode = (EditText)findViewById(R.id.checkCodeContent);
        //获取验证码事件
        Button getCodeButton =(Button)findViewById(R.id.getCheckCodeButton);
        getCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phone.getText().toString()==null||phone.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this,R.string.pleaseFillPhone,Toast.LENGTH_SHORT).show();
                    return;
                }
                final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, getResources().getString(R.string.connecting), getResources().getString(R.string.connectingToServerPleaseWait));
                new GetCode(phone.getText().toString(),new GetCode.SuccessCallback() {
                    @Override
                    public void onSuccess(String result) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,R.string.getCheckCodeSucesPleaseCheck,Toast.LENGTH_SHORT).show();
                    }
                },new GetCode.FailCallback(){
                    @Override
                    public void onFail(){
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,R.string.getCheckCodeFail,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        Button loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(phone.getText().toString())){
                    Toast.makeText(LoginActivity.this,R.string.pleaseFillPhone,Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(checkCode.getText().toString())){
                    Toast.makeText(LoginActivity.this,R.string.pleaseFillCheckCode,Toast.LENGTH_SHORT).show();
                }else{
                    final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, getResources().getString(R.string.logining), getResources().getString(R.string.loginingPleaseWait));
                    new Login(phone.getText().toString(),checkCode.getText().toString(),new Login.SuccessCallback(){
                        @Override
                        public void onSuccess(String token){
                            progressDialog.dismiss();
                            Config.cachePhone(LoginActivity.this, phone.getText().toString());
                            Config.cacheToke(LoginActivity.this,token);
                            Intent timeLine = new Intent(LoginActivity.this,TimeLineActivity.class);
                            timeLine.putExtra(Config.KEY_PHONE,phone.getText().toString());
                            timeLine.putExtra(Config.KEY_TOKEN,token);
                            startActivity(timeLine);
                        }
                    },new Login.FailCallback(){
                        @Override
                        public void onFail(){
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this,R.string.checkCodeIsNotCorrect,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
