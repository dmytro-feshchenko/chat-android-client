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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private ArrayList<Conversation> conversationsList;

    private ChatAdapter chatAdapter;

    private EditText text;

    private ChatUser interviewer;

    private Date lastMessageDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        conversationsList = new ArrayList<Conversation>();
        ListView list = (ListView) findViewById(R.id.list);

        chatAdapter = new ChatAdapter();
        list.setAdapter(chatAdapter);
        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setStackFromBottom(true);

        text = (EditText) findViewById(R.id.txt);
        text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        setTouchNClick(R.id.btnSend);

        interviewer = (ChatUser) getIntent().getSerializableExtra(Const.EXTRA_DATA);

        ActionBar actionBar = getActionBar();
        if (actionBar != null)
            actionBar.setTitle(interviewer.getUsername());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadConversationList();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btnSend)
            sendMessage();
    }

    /**
     * Send message to interviewer.
     * If message is empty - ignore it.
     * Otherwise - send message to server.
     */
    private void sendMessage() {
        if (text.length() == 0)
            return;

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(text.getWindowToken(), 0);

        String message = this.text.getText().toString();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            final Conversation conversation = new Conversation(message,
                    Calendar.getInstance().getTime(), user.getUid(),
                    interviewer.getId(), "");

            conversation.setStatus(Conversation.STATUS_SENDING);
            conversationsList.add(conversation);
            final String key = FirebaseDatabase.getInstance()
                    .getReference("messages")
                    .push().getKey();
            FirebaseDatabase.getInstance().getReference("messages").child(key)
                    .setValue(conversation)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Conversation conv = conversationsList.get(conversationsList.indexOf(conversation));
                            if (task.isSuccessful()) {
                                conv.setStatus(Conversation.STATUS_SENT);
                            }
                            else {
                                conv.setStatus(Conversation.STATUS_FAILED);
                            }

                            FirebaseDatabase.getInstance()
                                    .getReference("messages")
                                    .child(key).setValue(conv)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            chatAdapter.notifyDataSetChanged();
                                        }
                                    });
                        }
                    });
        }
        chatAdapter.notifyDataSetChanged();
        text.setText(null);
    }

    /**
     * Load the conversation list from Parse server and save the date of last
     * message that will be used to load only recent new messages
     */
    private void loadConversationList() {
        FirebaseDatabase.getInstance().getReference("messages").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        Conversation conversation = ds.getValue(Conversation.class);
                        if (conversation.getReceiver().contentEquals(user.getUid()) || conversation.getSender().contentEquals(user.getUid())) {
                            conversationsList.add(conversation);
                            if (lastMessageDate == null || lastMessageDate.before(conversation.getDate()))
                                lastMessageDate = conversation.getDate();
                            chatAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * The Class ChatAdapter is the adapter class for Chat ListView. This
     * adapter shows the Sent or Received Chat message in each list item.
     */
    private class ChatAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return conversationsList.size();
        }

        @Override
        public Conversation getItem(int position) {
            return conversationsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int pos, View v, ViewGroup arg2) {
            Conversation c = getItem(pos);
            if (c.isSent())
                v = getLayoutInflater().inflate(R.layout.chat_item_sent, null);
            else
                v = getLayoutInflater().inflate(R.layout.chat_item_rcv, null);

            TextView lbl = (TextView) v.findViewById(R.id.lbl1);
            lbl.setText(DateUtils.getRelativeDateTimeString(Chat.this, c
                            .getDate().getTime(), DateUtils.SECOND_IN_MILLIS,
                    DateUtils.DAY_IN_MILLIS, 0));

            lbl = (TextView) v.findViewById(R.id.lbl2);
            lbl.setText(c.getMsg());

            lbl = (TextView) v.findViewById(R.id.lbl3);
            if (c.isSent()) {
                if (c.getStatus() == Conversation.STATUS_SENT)
                    lbl.setText(R.string.delivered_text);
                else {
                    if (c.getStatus() == Conversation.STATUS_SENDING)
                        lbl.setText(R.string.sending_text);
                    else {
                        lbl.setText(R.string.failed_text);
                    }
                }
            } else
                lbl.setText("");

            return v;
        }
    }
}
