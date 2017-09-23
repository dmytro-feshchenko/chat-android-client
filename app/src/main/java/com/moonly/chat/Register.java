package com.moonly.chat;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.moonly.chat.custom.CustomActivity;
import com.moonly.chat.model.ChatUser;
import com.moonly.chat.utils.Utils;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Register extends CustomActivity {
    private EditText username;

    private EditText email;

    private EditText password;

    private ProgressDialog registerProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        setTouchNClick(R.id.btnRegistration);

        username = (EditText) findViewById(R.id.username);

        email = (EditText) findViewById(R.id.email);

        password = (EditText) findViewById(R.id.password);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        // extract form fields
        final String username = this.username.getText().toString();
        final String email = this.email.getText().toString();
        final String password = this.password.getText().toString();

        // validate data
        if (username.length() == 0 || email.length() == 0 || password.length() == 0) {
            Utils.showDialog(this, R.string.err_fields_empty);
            return;
        }

        // create new user in database
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Logger.getLogger(Register.class.getName()).log(Level.ALL, "createUserWithEmailAndPassword:onComplete:" + task.isSuccessful());
                        registerProgressDialog.dismiss();
                        if (!task.isSuccessful()) {
                            Logger.getLogger(Register.class.getName()).log(Level.ALL, "createUserWithEmailAndPassword", task.getException());
                            Utils.showDialog(Register.this, getString(R.string.err_singup));
                        }
                        else {
                            final ArrayList<String> defaultRoom = new ArrayList<String>();
                            defaultRoom.add("home");

                            // update user profile
                            final FirebaseUser user = task.getResult().getUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .setPhotoUri(Uri.parse("https://pickaface.net/assets/images/slides/slide4.png"))
                                    .build();
                            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Logger.getLogger(Register.class.getName()).log(Level.ALL, "User profile updated.");
                                        // Build ChatUser Model
                                        UserList.user = new ChatUser(user.getUid(), username, email, true, defaultRoom);
                                        // Setup link to users database
                                        FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).setValue(UserList.user);
                                        startActivity(new Intent(Register.this, UserList.class));
                                        finish();
                                    }
                                }
                            });
                        }
                    }
                });

        registerProgressDialog = ProgressDialog.show(this, null, getString(R.string.alert_wait));

    }
}
