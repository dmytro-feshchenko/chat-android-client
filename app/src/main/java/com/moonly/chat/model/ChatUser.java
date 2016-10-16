package com.moonly.chat.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by dmitryi on 10/16/16.
 * Class that represents a single chat user.
 */
@IgnoreExtraProperties
public class ChatUser implements Serializable {
    private String id;
    private String username;
    private String email;
    private Boolean online;
    private ArrayList<String> room;

    /**
     * Default constructor
     */
    public ChatUser() {

    }

    public ChatUser(String id, String username, String email, Boolean online, ArrayList<String> room) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.online = online;
        this.room = room;
    }

    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Boolean isOnline() {
        return online;
    }

    public ArrayList<String> getRoom() {
        return room;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public void setRoom(ArrayList<String> room) {
        this.room = room;
    }
}
