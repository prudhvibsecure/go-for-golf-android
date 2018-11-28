package com.sharmas.golf_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sharmas.golf_android.adapter.OffersAdapter;
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
 * Created by Admin on 2017-11-08.
 */

public class MoreOffers extends AppCompatActivity implements IItemHandler, OffersAdapter.OffersAdapterListener {
    private List<GolfStatesModel> statesList;
    private OffersAdapter adapter;
    private Bundle extras;
    private String gid;
    private ListView statListView, offerview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commonlist);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.gfg_back_btn);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ((TextView)findViewById(R.id.title_in)).setText("GENERAL DISCOUNT OFFERS");
        extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        gid = extras.getString("gid");
        golfDetails();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_tes, menu);
        menu.findItem(R.id.tv_home).setVisible(false);
        return true;
    }

    private void golfDetails() {

        try {
            String url = AppSettings.getInstance(this).getPropertyValue("golf_details");
            JSONObject object = new JSONObject();
            object.put("golfid", gid);

            HTTPostJson post = new HTTPostJson(this, this, object.toString(), 1);
            post.setContentType("application/json");
            post.execute(url, "");
            Utils.showProgress(getString(R.string.loading), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                MoreOffers.this.finish();

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

                        JSONObject object = new JSONObject(results.toString());
                        if (object.has("status") && object.optString("status").equalsIgnoreCase("0")) {
                            JSONArray array = object.getJSONArray("golfcourse_detail");
                            JSONObject jsonobject = array.getJSONObject(0);
                            JSONArray offerobject = jsonobject.getJSONArray("offerdetails");
                            if (offerobject.length() == 0) {
                                ((TextView) findViewById(R.id.spoff_viewmore)).setVisibility(View.VISIBLE);
                            } else {

                                for (int j = 0; j < offerobject.length(); j++) {

                                    GolfStatesModel map = new GolfStatesModel();
                                    JSONObject jsonte = offerobject.getJSONObject(j);
                                    String sname = jsonte.getString("offername");
                                    String edate = jsonte.getString("expiry");
                                    String offerid = jsonte.getString("offerid");
                                    String golfid = jsonte.getString("golfid");
                                    String odescription = jsonte.getString("odescription");

                                    map.setStateName(sname);
                                    map.setDesc(odescription);
                                    map.setExpdate(edate);
                                    map.setGolfId(golfid);
                                    map.setOfferId(offerid);
                                    statesList.add(map);

                                    statListView = (ListView) findViewById(R.id.list_ofers_new);
                                    adapter = new OffersAdapter(this, statesList, this);
                                    statListView.setAdapter(adapter);
                                    ViewCompat.setNestedScrollingEnabled(statListView, true);

                                }
                            }
                        }


                    }
                    break;
                case 3:
                    if (results != null) {

                        JSONObject object = new JSONObject(results.toString());
                        if (object.has("status") && object.optString("status").equalsIgnoreCase("0")) {
                            showToast(object.optString("statusdescription"));
                        } else {
                            showToast(object.optString("statusdescription"));
                            Intent renual = new Intent(getApplicationContext(), RenualPage.class);
                            startActivity(renual);
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

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onError(String errorCode, int requestType) {

    }

    @Override
    public void onMessageReedmClicked(int position) {

    }

    @Override
    public void onMessageReedmClicked(String golfId, String offerId) {
        try {
            String url = AppSettings.getInstance(this).getPropertyValue("reedeem_offer");
            JSONObject object = new JSONObject();
            object.put("email", AppPreferences.getInstance(this).getFromStore("email"));
            object.put("golfid", golfId);
            object.put("offerid", offerId);

            HTTPostJson post = new HTTPostJson(this, this, object.toString(), 3);
            post.setContentType("application/json");
            post.execute(url, "");
            Utils.showProgress(getString(R.string.loading), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
