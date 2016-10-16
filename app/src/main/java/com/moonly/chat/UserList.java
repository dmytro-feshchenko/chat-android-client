package com.moonly.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.moonly.chat.custom.CustomActivity;
import com.moonly.chat.model.ChatUser;

import java.util.ArrayList;

/**
 * Created by dmitryi on 10/16/16.
 * Activity that shows list of all users of the app.
 * Shows status (Online, Offline) of the users.
 */
public class UserList extends CustomActivity {
    /** Reference to users database */
    DatabaseReference databaseReference;

    /** List of chat users */
    private ArrayList<ChatUser> uList;

    /** The user */
    public static ChatUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);
    }
}
