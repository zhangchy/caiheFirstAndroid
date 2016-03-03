package com.jikexueyuan.secret.bean;

/**
 * Created by 13058 on 2016/3/3.
 */
public class Comment {
    public Comment(String phone_md5,String content){
        this.content = content;
        this.phone_md5 = phone_md5;
    }
    private String content;
    private String phone_md5;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhone_md5() {
        return phone_md5;
    }

    public void setPhone_md5(String phone_md5) {
        this.phone_md5 = phone_md5;
    }
}
