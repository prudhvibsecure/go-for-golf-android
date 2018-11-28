package com.sharmas.golf_android;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.sharmas.golf_android.common.AppPreferences;

public class Golf_Landing extends AppCompatActivity implements View.OnClickListener {

    private Dialog mDiloag = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        findViewById(R.id.register).setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.membership).setOnClickListener(this);
        findViewById(R.id.browse_tv).setOnClickListener(this);

        sessonMe();

    }

    private void sessonMe() {
        String email = AppPreferences.getInstance(this).getFromStore("email");
        if (email.length() == 0) {
            ((TextView)findViewById(R.id.login)).setText("LOG IN");
        } else {
            ((TextView)findViewById(R.id.login)).setText("HOME");
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.browse_tv:
                // lancherActivity();
                lancherActivity(GolfStates.class);
                break;
            case R.id.membership:
               showMemberDesc();
                break;
            case R.id.register:
                lancherActivity(Registration.class);
                break;
            case R.id.login:
//                String email = AppPreferences.getInstance(this).getFromStore("email");
//                if (email.length() == 0) {
                    lancherActivity(Login.class);
//                } else {
//                    lancherActivity(GolfStates.class);
//                }
                break;

        }

    }

    private void showMemberDesc() {
        mDiloag = new Dialog(this);
        mDiloag.setContentView(R.layout.member_ship_ll);
        ((TextView) mDiloag.findViewById(R.id.tx_member_data)).setText(Html.fromHtml("Why take a Go for Golf <br/><br/>membership? <br/><br/>That's easy.<br/><br/>Australian golf courses have joined Go for Golf to bring you special deals that could save you anywhere from several hundred to thousands of dollars.<br/><br/>Why not explore new courses at a fraction of the regular price. In addition, you will also receive periodic special offers.<br/><br/>As an Introductory Offer Membership is FREE!!."));
        ((TextView) mDiloag.findViewById(R.id.m_register)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDiloag.cancel();
                lancherActivity(Registration.class);
            }
        });
        mDiloag.show();
    }

    private void lancherActivity(Class<?> cls) {
        Intent pass = new Intent(getApplicationContext(), cls);
        startActivity(pass);

    }

    @Override
    protected void onResume() {
        sessonMe();
        super.onResume();
    }
}
