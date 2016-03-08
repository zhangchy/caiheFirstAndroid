package com.jikexueyuan.secret.bean;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.webkit.WebView;

import com.jikexueyuan.secret.secret.R;

import org.json.JSONArray;
import org.xmlpull.v1.XmlPullParserException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 13058 on 2016/3/7.
 */
public class User {
    private String name;
    private String age;
    private String sex;
    private Context context;
    private WebView webView;
    public User(Context context,WebView webView){
        this.context = context;
        this.webView = webView;
    }
    public User(){
    }
    public User(String name,String age,String sex){
        this.name = name;
        this.age = age;
        this.sex = sex;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
