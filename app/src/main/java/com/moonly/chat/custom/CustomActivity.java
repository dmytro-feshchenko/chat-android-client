package com.moonly.chat.custom;

import android.app.ActionBar;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.moonly.chat.R;
import com.moonly.chat.utils.TouchEffect;

import android.os.Bundle;

/**
 * This is a common activity that all other activities of the app can extend to
 * inherit the common behaviors like implementing a common interface that can be
 * used in all child activities.
 */
public class CustomActivity extends FragmentActivity implements OnClickListener {

    /**
     * Apply this constant as touch listener to provide
     * custom alpha touch effect.
     */
    public static final TouchEffect TOUCH = new TouchEffect();

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setupActionBar();
    }

    protected void setupActionBar() {
        final ActionBar actionBar = getActionBar();
        if (actionBar == null)
            return;
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setLogo(R.drawable.icon);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    /**
     * Sets the touch and click listener for a view with given id.
     * @param id the identifier
     * @return the view on which listeners applied
     */
    public View setTouchNClick(int id) {
        View v = setClick(id);
        if (v != null)
            v.setOnTouchListener(TOUCH);
        return v;
    }

    /**
     * Sets the click listener for a view with given id.
     * @param id the identifier
     * @return the view on which listeners applied
     */
    public View setClick(int id) {
        View v = findViewById(id);
        if (v != null)
            v.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {

    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
}
