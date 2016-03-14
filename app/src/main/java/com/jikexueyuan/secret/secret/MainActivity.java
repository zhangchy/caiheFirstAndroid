package com.jikexueyuan.secret.secret;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import com.jikexueyuan.secret.activity.LoginActivity;
import com.jikexueyuan.secret.activity.TimeLineActivity;
import com.jikexueyuan.secret.common.Config;
import com.jikexueyuan.secret.common.UIHandler;

import java.util.Locale;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String token = Config.getCachedToken(this);
        String phoneNum = Config.getCachedPhone(this);
        // startActivity(new Intent(this,TimeLineActivity.class));
        if(TextUtils.isEmpty(token)||TextUtils.isEmpty(phoneNum)){
            //从一个activity跳转到另一个activity
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this,TimeLineActivity.class);
            intent.putExtra(Config.KEY_PHONE,phoneNum);
            intent.putExtra(Config.KEY_TOKEN,token);
            startActivity(intent);
        }
        //返回的空页面 去掉
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
