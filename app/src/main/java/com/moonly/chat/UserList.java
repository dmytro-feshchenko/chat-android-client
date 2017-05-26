package com.moonly.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.moonly.chat.custom.CustomActivity;
import com.moonly.chat.model.ChatUser;
import com.moonly.chat.utils.Const;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by dmitryi on 10/16/16.
 * Activity that shows list of all users of the app.
 * Shows status (Online, Offline) of the users.
 */
public class UserList extends CustomActivity {
    // List of chat users
    private ArrayList<ChatUser> uList;

    // The user
    public static ChatUser user;


}
