package com.moonly.chat.model;

import java.util.Date;

/**
 * Created by dmitryi on 10/16/16.
 * Class that represents single chat message.
 */
public class Conversation {
    public static final int STATUS_SENDING = 0;
    public static final int STATUS_SENT = 1;
    public static final int STATUS_FAILED = 2;

    private String msg;

    private int status = STATUS_SENT;

    private Date date;

    private String sender;

    private String receiver;

    private String photoUrl;

    public Conversation(String msg, Date date, String sender, String receiver, String photoUrl) {
        this.msg = msg;
        this.date = date;
        this.sender = sender;
        this.receiver = receiver;
        this.photoUrl = photoUrl;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSent () {
        return UserList
    }

    public String getSender() {
        return this.sender;
    }
}
