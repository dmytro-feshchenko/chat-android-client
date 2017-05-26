package com.moonly.chat;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

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
}
