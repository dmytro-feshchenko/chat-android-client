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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    /** Reference to users database */
    DatabaseReference database;

    /** List of chat users */
    private ArrayList<ChatUser> uList;

    /** The user */
    public static ChatUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);

        database = FirebaseDatabase.getInstance().getReference();

        getActionBar().setDisplayHomeAsUpEnabled(false);

        updateUserStatus(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateUserStatus(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserList();
    }

    /**
     * Update user status
     * @param online true if user is online
     */
    private void updateUserStatus(boolean online) {
        // TODO: Add user status updates
    }

    /**
     * Load list of users
     */
    private void loadUserList() {
        final ProgressDialog dia = ProgressDialog.show(this, null,
                getString(R.string.alert_loading));

        // pull the users list
        database.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dia.dismiss();
                long size = dataSnapshot.getChildrenCount();
                if (size == 0) {
                    Toast.makeText(UserList.this,
                            R.string.msg_no_user_found,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                uList = new ArrayList<ChatUser>();
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    ChatUser user = ds.getValue(ChatUser.class);
                    Logger.getLogger(UserList.class.getName()).log(Level.ALL, user.getUsername());
                    if (!user.getId().contentEquals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                        uList.add(user);
                }
                ListView list = (ListView) findViewById(R.id.list);
                list.setAdapter(new UserAdapter());
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        startActivity(new Intent(UserList.this, Chat.class).putExtra(Const.EXTRA_DATA, uList.get(position)));
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private class UserAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return uList.size();
        }

        @Override
        public ChatUser getItem(int position) {
            return uList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.chat_item, null);
            }

            ChatUser user = getItem(position);
            TextView lbl = (TextView) convertView;
            lbl.setText(user.getUsername());
            lbl.setCompoundDrawablesWithIntrinsicBounds(
                    user.isOnline()? R.drawable.ic_online: R.drawable.ic_offline,
                    0, R.drawable.arrow, 0);
            return convertView;
        }
    }
}
