package com.sharmas.golf_android;

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

import com.sharmas.golf_android.adapter.SpecialOfferAdapter;
import com.sharmas.golf_android.callbacks.IItemHandler;
import com.sharmas.golf_android.common.AppSettings;
import com.sharmas.golf_android.models.OffersModel;
import com.sharmas.golf_android.network.HTTPostJson;
import com.sharmas.golf_android.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017-11-08.
 */

public class MoreSpecialOffers extends AppCompatActivity implements IItemHandler {
    private SpecialOfferAdapter spAdapter;
    private Bundle extras;
    private List<OffersModel> offersModelList;
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
        ((TextView)findViewById(R.id.title_in)).setText("SPECIAL OFFERS");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                MoreSpecialOffers.this.finish();

                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
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
    public void onFinish(Object results, int requestType) {
        Utils.dismissProgress();
        offersModelList = new ArrayList<OffersModel>();

        try {

            switch (requestType) {

                case 1:
                    if (results != null) {

                        JSONObject object = new JSONObject(results.toString());
                        if (object.has("status") && object.optString("status").equalsIgnoreCase("0")) {
                            JSONArray array = object.getJSONArray("golfcourse_detail");
                            JSONObject jsonobject = array.getJSONObject(0);
                            JSONArray spofferobject = jsonobject.getJSONArray("specialdetails");
                            if (spofferobject.length() == 0) {
                                ((TextView) findViewById(R.id.spoff_viewmore)).setVisibility(View.VISIBLE);
                            } else {
                                for (int j = 0; j < spofferobject.length(); j++) {

                                    OffersModel map = new OffersModel();
                                    JSONObject jsonte = spofferobject.getJSONObject(j);
                                    String specialid = jsonte.getString("specialid");
                                    String golfid = jsonte.getString("golfid");
                                    String specialname = jsonte.getString("specialname");
                                    String sdescription = jsonte.getString("sdescription");

                                    map.setGolfid(golfid);
                                    map.setSpecialid(specialid);
                                    map.setSpecialname(specialname);
                                    map.setSdescription(sdescription);
                                    offersModelList.add(map);

                                    offerview = (ListView) findViewById(R.id.list_ofers_new);
                                    spAdapter = new SpecialOfferAdapter(this, offersModelList);
                                    offerview.setAdapter(spAdapter);
                                    ViewCompat.setNestedScrollingEnabled(offerview, true);

                                }
                            }
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

}
