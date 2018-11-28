package com.sharmas.golf_android;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.sharmas.golf_android.callbacks.IItemHandler;
import com.sharmas.golf_android.common.AppPreferences;
import com.sharmas.golf_android.common.AppSettings;
import com.sharmas.golf_android.network.HTTPostJson;
import com.sharmas.golf_android.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by user on 3/15/2017.
 */

public class Login extends AppCompatActivity implements View.OnClickListener, IItemHandler, CompoundButton.OnCheckedChangeListener {
    private Dialog mDilaog = null;
    private CheckBox remberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        findViewById(R.id.tv_login).setOnClickListener(this);
        findViewById(R.id.check_pass).setOnClickListener(this);
        remberMe = (CheckBox) findViewById(R.id.check_rember);

        boolean tag = getFromSP("remberMe");
        if (tag == true) {
            ((EditText) findViewById(R.id.user_name_email)).setText(AppPreferences.getInstance(this).getFromStore("uemail"));
            ((EditText) findViewById(R.id.user_password)).setText(AppPreferences.getInstance(this).getFromStore("pwd"));
        }
        remberMe.setChecked(tag);
        remberMe.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_login:
                /** When user click this login button
                 * login service is call from below method*/
                loginRequest();
                break;
            case R.id.check_pass:

                Intent forgot = new Intent(this, ForgotPassword.class);
                startActivity(forgot);
//                mDilaog = new Dialog(this);
//                mDilaog.setContentView(R.layout.forget_password);
//                mDilaog.setTitle("Forgot Password");
//                mDilaog.setCancelable(true);
//                mDilaog.setCanceledOnTouchOutside(true);
//                mDilaog.findViewById(R.id.forgot_submit).setOnClickListener(onClick);
//                mDilaog.show();
                break;
        }

    }

    View.OnClickListener onClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);

            switch (view.getId()) {

                case R.id.forgot_submit:
                    forgotPassword();
                    break;

                default:
                    break;
            }

        }
    };

    private void forgotPassword() {

        try {
            String email = ((EditText) mDilaog.findViewById(R.id.email_address)).getText().toString().trim();

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
            HTTPostJson post = new HTTPostJson(this, this, object.toString(), 2);
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

    private void loginRequest() {

        try {

            String name = ((EditText) findViewById(R.id.user_name_email)).getText().toString().trim();

            if (name.length() == 0) {
                showToast(R.string.lvalid1);
                findViewById(R.id.user_name_email).requestFocus();
                return;
            }

            String password = ((EditText) findViewById(R.id.user_password)).getText().toString().trim();

            if (password.length() == 0) {
                showToast(R.string.lvalid2);
                findViewById(R.id.user_password).requestFocus();
                return;
            }
            String url = AppSettings.getInstance(this).getPropertyValue("user_login");
            JSONObject object = new JSONObject();
            object.put("username", name);
            object.put("password", password);

            HTTPostJson post = new HTTPostJson(this, this, object.toString(), 1);
            post.setContentType("application/json");
            post.execute(url, "");
            Utils.showProgress(getString(R.string.loading), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showToast(String text) {

        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void showToast(int text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinish(Object results, int requestType) {
        Utils.dismissProgress();

        try {

            switch (requestType) {
                case 1:
                    JSONObject jsonObject = new JSONObject(results.toString());
                    if (jsonObject.has("status") && jsonObject.optString("status").equalsIgnoreCase("0")) {
                        showToast(jsonObject.optString("statusdescription"));
                        JSONArray jsonArray = jsonObject.getJSONArray("member_detail");
                        JSONObject jsonobject = jsonArray.getJSONObject(0);
                        AppPreferences.getInstance(Login.this).addToStore("email", jsonobject.optString("email"));
                        lancherActivity(GolfStates.class);
                        Login.this.finish();
                        return;

                    }
                    showToast(jsonObject.optString("statusdescription"));
                    break;
                case 2:
                    JSONObject Object = new JSONObject(results.toString());

                    if (Object.has("status") && Object.optString("status").equalsIgnoreCase("0")) {
                        showToast(Object.optString("statusdescription"));
                        mDilaog.dismiss();
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

    private void lancherActivity(Class<?> cls) {
        Intent pass = new Intent(getApplicationContext(), cls);
        startActivity(pass);

    }

    @Override
    public void onError(String errorCode, int requestType) {
        showToast(errorCode);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.check_rember:
                saveInSp("remberMe", isChecked);
                String email = ((EditText) findViewById(R.id.user_name_email)).getText().toString().trim();
                if (email.length() == 0) {
                    showToast(R.string.lvalid1);
                    return;
                } else {
                    AppPreferences.getInstance(this).addToStore("uemail", email);

                }
                String password = ((EditText) findViewById(R.id.user_password)).getText().toString().trim();
                if (password.length() == 0) {
                    showToast(R.string.lvalid2);
                    return;
                } else {
                    AppPreferences.getInstance(this).addToStore("pwd", password);

                }

                break;
        }
    }

    private boolean getFromSP(String key) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Golf", android.content.Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }

    private void saveInSp(String key, boolean value) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Golf", android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}
