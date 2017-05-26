package com.moonly.chat;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.moonly.chat.custom.CustomActivity;
import com.moonly.chat.model.ChatUser;
import com.moonly.chat.model.Conversation;
import com.moonly.chat.utils.Const;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by dmitryi on 10/16/16.
 * Activity class that holds main chat screen. It shows
 * all the conversation messages between two users and
 * also allows the user to send and receive messages.
 */
public class Chat extends CustomActivity {

}
