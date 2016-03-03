package com.jikexueyuan.secret.bean;

/**
 * Created by 13058 on 2016/3/3.
 */
public class Message {
    private String msgId;
    private String msg;
    private String phone_md5;
    public Message(String phone_md5,String msg,String msgId){
        this.msg = msg;
        this.msgId = msgId;
        this.phone_md5 = phone_md5;
    }
    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPhone_md5() {
        return phone_md5;
    }

    public void setPhone_md5(String phone_md5) {
        this.phone_md5 = phone_md5;
    }
}
