package com.sharmas.golf_android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sharmas.golf_android.callbacks.IItemHandler;
import com.sharmas.golf_android.common.AppSettings;
import com.sharmas.golf_android.network.HTTPostJson;
import com.sharmas.golf_android.utils.Utils;

import org.json.JSONObject;

/**
 * Created by Admin on 2017-11-07.
 */

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener, IItemHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);
        ((TextView)findViewById(R.id.forgot_title)).setText("Forgot Password");
        findViewById(R.id.forgot_submit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.forgot_submit:
                forgotPassword();
                break;
        }
    }

    private void forgotPassword() {

        try {
            String email = ((EditText) findViewById(R.id.email_address)).getText().toString().trim();

            if (email.length() == 0) {
                showToast(R.string.lvalid2);
                findViewById(R.id.new_pass).requestFocus();
                return;
            }
            if (!emailValidation(email)) {
                showToast("Please Enter Valid Email");
                // ((EditText) findViewById(R.id.et_ceid)).requestFocus();
                return;
            }
            String url = AppSettings.getInstance(this).getPropertyValue("forget_pass");
            JSONObject object = new JSONObject();
            object.put("email", email);
            HTTPostJson post = new HTTPostJson(this, this, object.toString(), 1);
            post.setContentType("application/json");
            post.execute(url, "");
            Utils.showProgress(getString(R.string.loading), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean emailValidation(String email) {

        if (email == null || email.length() == 0 || email.indexOf("@") == -1 || email.indexOf(" ") != -1) {
            return false;
        }
        int emailLenght = email.length();
        int atPosition = email.indexOf("@");

        String beforeAt = email.substring(0, atPosition);
        String afterAt = email.substring(atPosition + 1, emailLenght);

        if (beforeAt.length() == 0 || afterAt.length() == 0) {
            return false;
        }
        if (email.charAt(atPosition - 1) == '.') {
            return false;
        }
        if (email.charAt(atPosition + 1) == '.') {
            return false;
        }
        if (afterAt.indexOf(".") == -1) {
            return false;
        }
        char dotCh = 0;
        for (int i = 0; i < afterAt.length(); i++) {
            char ch = afterAt.charAt(i);
            if ((ch == 0x2e) && (ch == dotCh)) {
                return false;
            }
            dotCh = ch;
        }
        if (afterAt.indexOf("@") != -1) {
            return false;
        }
        int ind = 0;
        do {
            int newInd = afterAt.indexOf(".", ind + 1);

            if (newInd == ind || newInd == -1) {
                String prefix = afterAt.substring(ind + 1);
                if (prefix.length() > 1 && prefix.length() < 20) {
                    break;
                } else {
                    return false;
                }
            } else {
                ind = newInd;
            }
        } while (true);
        dotCh = 0;
        for (int i = 0; i < beforeAt.length(); i++) {
            char ch = beforeAt.charAt(i);
            if (!((ch >= 0x30 && ch <= 0x39) || (ch >= 0x41 && ch <= 0x5a) || (ch >= 0x61 && ch <= 0x7a) || (ch == 0x2e)
                    || (ch == 0x2d) || (ch == 0x5f))) {
                return false;
            }
            if ((ch == 0x2e) && (ch == dotCh)) {
                return false;
            }
            dotCh = ch;
        }
        return true;
    }

    @Override
    public void onFinish(Object results, int requestType) {
        Utils.dismissProgress();

        try {

            switch (requestType) {

                case 1:
                    JSONObject Object = new JSONObject(results.toString());

                    if (Object.has("status") && Object.optString("status").equalsIgnoreCase("0")) {
                        showToast(Object.optString("statusdescription"));
                        finish();
                    }
                    showToast(Object.optString("statusdescription"));

                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String errorCode, int requestType) {

    }

    private void showToast(String text) {

        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void showToast(int text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
