package com.sharmas.golf_android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
 * Created by Admin on 2017-03-28.
 */

public class ChangePassword extends AppCompatActivity implements IItemHandler, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        findViewById(R.id.cpas_submit).setOnClickListener(this);

    }

    private void changePassword() {

        try {
            String new_pass = ((EditText) findViewById(R.id.new_pass)).getText().toString().trim();
            if (new_pass.length() == 0) {
                showToast("Please Enter Your Password");
                ((EditText) findViewById(R.id.new_pass)).requestFocus();
                return;
            }
//            if (new_pass.length() < 8 || new_pass.length() > 16) {
//                showToast(R.string.psmbc);
//                ((EditText) findViewById(R.id.new_pass)).requestFocus();
//                return;
//            }

            String conf_pass = ((EditText) findViewById(R.id.con_pass)).getText().toString().trim();
            if (conf_pass.length() == 0) {
                showToast("Please Enter Your Confirm Password");
                ((EditText) findViewById(R.id.con_pass)).requestFocus();
                return;
            }
//            if (conf_pass.length() < 8 || conf_pass.length() > 16) {
//                showToast(R.string.psmbc);
//                ((EditText) findViewById(R.id.con_pass)).requestFocus();
//                return;
//            }
            if (!new_pass.equals(conf_pass)) {
                showToast("Confirm Password must Match");
                ((EditText) findViewById(R.id.con_pass)).requestFocus();
                return;
            }
            String url = AppSettings.getInstance(this).getPropertyValue("change_pass");
            JSONObject object = new JSONObject();
            object.put("email", AppPreferences.getInstance(this).getFromStore("email"));
            object.put("password", new_pass);
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
                        ChangePassword.this.finish();
                        return;

                    }
                    showToast(jsonObject.optString("statusdescription"));
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
        showToast(errorCode);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cpas_submit:
                if (AppPreferences.getInstance(this).getFromStore("email").length() == 0) {
                    showToast("Please Login...");
                } else {
                    changePassword();
                }
                break;
        }

    }
}
