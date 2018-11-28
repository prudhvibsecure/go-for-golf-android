package com.sharmas.golf_android;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sharmas.golf_android.adapter.SubAreaAdapter;
import com.sharmas.golf_android.callbacks.IItemHandler;
import com.sharmas.golf_android.common.AppPreferences;
import com.sharmas.golf_android.common.AppSettings;
import com.sharmas.golf_android.models.GolfStatesModel;
import com.sharmas.golf_android.network.HTTPostJson;
import com.sharmas.golf_android.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017-03-24.
 */

public class Golf_Regions extends AppCompatActivity implements IItemHandler {

    private SubAreaAdapter adapter;
    private List<GolfStatesModel> statesList;
    private ListView statListView;
    private SwipeRefreshLayout mSwipeRefreshLayout = null;
    private Dialog mDilaog = null;
    private Bundle extras;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statelist);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setNavigationIcon(R.mipmap.gfg_back_btn);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //findViewById(R.id.view1).setVisibility(View.VISIBLE);
        //findViewById(R.id.view2).setVisibility(View.VISIBLE);
        findViewById(R.id.title_name).setVisibility(View.VISIBLE);


        extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        ((TextView) findViewById(R.id.title_name)).setText("REGIONS OF "+extras.getString("stateName"));

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimaryDark,
                R.color.colorPrimaryDark, R.color.colorPrimaryDark, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                getData();
                Utils.dismissProgress();

            }
        });
        getData();


    }

    private void getData() {
        try {

            String url = AppSettings.getInstance(this).getPropertyValue("regiondetails");
            JSONObject object = new JSONObject();
            object.put("stateid", extras.getString("stateid"));

            HTTPostJson post = new HTTPostJson(this, this, object.toString(), 1);
            post.setContentType("application/json");
            post.execute(url, "");
            Utils.showProgress(getString(R.string.loading), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_tes, menu);
        return true;
    }

    View.OnClickListener onClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);

            switch (view.getId()) {

                case R.id.tv_update_emails:
                    mDilaog.dismiss();
                    Intent tv_update_emails = new Intent(getApplicationContext(), UpdateEmailAddress.class);
                    startActivity(tv_update_emails);
                    break;

                case R.id.tv_update_pwd:
                    mDilaog.dismiss();
                    Intent pwd = new Intent(getApplicationContext(), ChangePassword.class);
                    startActivity(pwd);
                    // launchActivity(LoginInWithPin.class, 1002);
                    break;

                case R.id.tv_membership:
                    mDilaog.dismiss();
                    Intent reg = new Intent(getApplicationContext(), Registration.class);
                    startActivity(reg);
                    // launchActivity(LoginInWithPin.class, 1002);
                    break;

                case R.id.tv_share:
                    Intent in = new Intent(android.content.Intent.ACTION_SEND);
                    in.setType("text/plain");
                    in.putExtra(android.content.Intent.EXTRA_SUBJECT, "GO FOR GOLF");
                    in.putExtra(android.content.Intent.EXTRA_TEXT,
                            "To download GO FOR GOLF app, please click here https://play.google.com/store/apps/details?id=com.sharmas.golf_android");
                    startActivity(Intent.createChooser(in, "Share Via"));
                    // launchActivity(LoginInWithPin.class, 1002);
                    break;
                case R.id.tv_logout:
                    AppPreferences.getInstance(getApplicationContext()).addToStore("email", "");
                    mDilaog.dismiss();
                    Intent i1 = new Intent(getApplicationContext(), Login.class);
                    i1.setAction(Intent.ACTION_MAIN);
                    i1.addCategory(Intent.CATEGORY_HOME);
                    i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i1);
                    finish();
                    break;

                default:
                    break;
            }

        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Golf_Regions.this.finish();
                break;
            case R.id.tv_home:
                mDilaog = new Dialog(this);
                mDilaog.setContentView(R.layout.dialog_box_menu);
                mDilaog.setCancelable(true);
                mDilaog.setCanceledOnTouchOutside(true);
                mDilaog.findViewById(R.id.tv_update_emails).setOnClickListener(onClick);
                mDilaog.findViewById(R.id.tv_update_pwd).setOnClickListener(onClick);
                mDilaog.findViewById(R.id.tv_membership).setOnClickListener(onClick);
                mDilaog.findViewById(R.id.tv_share).setOnClickListener(onClick);
                mDilaog.findViewById(R.id.tv_logout).setOnClickListener(onClick);
                String email = AppPreferences.getInstance(this).getFromStore("email");
                if (email.length() == 0) {
                    mDilaog.findViewById(R.id.tv_logout).setVisibility(View.INVISIBLE);
                } else {
                    mDilaog.findViewById(R.id.tv_logout).setVisibility(View.VISIBLE);
                }
                mDilaog.show();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onFinish(Object results, int requestType) {
        Utils.dismissProgress();
        statesList = new ArrayList<GolfStatesModel>();
        try {

            switch (requestType) {

                case 1:
                    if (results != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mSwipeRefreshLayout.setEnabled(true);
                        JSONObject object = new JSONObject(results.toString());
                        if (object.has("status") && object.optString("status").equalsIgnoreCase("0")) {
                            JSONArray array = object.getJSONArray("golfcourse_detail");
                            for (int i = 0; i < array.length(); i++) {
                                GolfStatesModel map = new GolfStatesModel();
                                JSONObject jsonobject = array.getJSONObject(i);
                                String sname = jsonobject.getString("rname");
                                String sid = jsonobject.getString("rid");

                                map.setStateName(sname);
                                map.setStateId(sid);
                                statesList.add(map);
                            }
                            statListView = (ListView) findViewById(R.id.states_list);
                            adapter = new SubAreaAdapter(this, statesList);
                            statListView.setAdapter(adapter);
                        }
                        if (object.has("status") && object.optString("status").equalsIgnoreCase("1")) {

                            ((TextView) findViewById(R.id.no_data)).setVisibility(View.VISIBLE);
                            ((TextView) findViewById(R.id.no_data)).setText(object.optString("statusdescription"));
                            return;
                        }


                    }


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

    private void showToast(String txt) {
        Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();
    }


}