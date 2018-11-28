package com.sharmas.golf_android;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sharmas.golf_android.adapter.AmenitiesAdapter;
import com.sharmas.golf_android.adapter.OffersAdapter;
import com.sharmas.golf_android.adapter.SlidingImage_Adapter;
import com.sharmas.golf_android.adapter.SpecialOfferAdapter;
import com.sharmas.golf_android.callbacks.IItemHandler;
import com.sharmas.golf_android.common.AppPreferences;
import com.sharmas.golf_android.common.AppSettings;
import com.sharmas.golf_android.models.AminitesModel;
import com.sharmas.golf_android.models.GolfImageModel;
import com.sharmas.golf_android.models.GolfStatesModel;
import com.sharmas.golf_android.models.OffersModel;
import com.sharmas.golf_android.network.HTTPostJson;
import com.sharmas.golf_android.utils.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Admin on 2017-03-28.
 */

public class Golf_details extends AppCompatActivity implements IItemHandler, OffersAdapter.OffersAdapterListener, View.OnClickListener {
    private Bundle extras;
    private ImageView st_image_logo, st_main_img;
    private String gid;
    private ArrayList<GolfImageModel> mymvlist;
    private List<GolfImageModel> mvarraylist = null;
    private List<GolfStatesModel> statesList;
    private List<OffersModel> offersModelList;
    private List<AminitesModel> aminitesModelList;
    private ListView statListView, offerview, aminiteslist;
    private OffersAdapter adapter;
    private SpecialOfferAdapter spAdapter;
    private AmenitiesAdapter amiAdapter;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private Dialog mDialog;
    private String email, phone, website;
    private RelativeLayout offer_ll, sp_offers_ll, aminitis_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_view_main);

        TextView tv = (TextView) this.findViewById(R.id.mywidget);
        tv.setSelected(true);
        tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.gfg_back_btn);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        sp_offers_ll = (RelativeLayout) findViewById(R.id.off_ll);
        offer_ll = (RelativeLayout) findViewById(R.id.sp_off_ll);
        aminitis_ll = (RelativeLayout) findViewById(R.id.aminitis);
        st_image_logo = (ImageView) findViewById(R.id.logo_content);

        st_main_img = (ImageView) findViewById(R.id.image_slider);
        st_main_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getSlidePath();
            }
        });

        extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        gid = extras.getString("golfId");

        findViewById(R.id.off_more).setOnClickListener(this);
        findViewById(R.id.spoff_viewmore).setOnClickListener(this);
        golfDetails();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_tes, menu);
        menu.findItem(R.id.tv_home).setVisible(false);
        return true;
    }

    private void getSlidePath() {

        try {
            String url = AppSettings.getInstance(this).getPropertyValue("golf_images");
            JSONObject object = new JSONObject();
            object.put("golfid", gid);

            HTTPostJson post = new HTTPostJson(this, this, object.toString(), 2);
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

                Golf_details.this.finish();

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
        statesList = new ArrayList<GolfStatesModel>();
        mvarraylist = new ArrayList<GolfImageModel>();
        offersModelList = new ArrayList<OffersModel>();
        aminitesModelList = new ArrayList<AminitesModel>();
        try {

            switch (requestType) {

                case 1:
                    if (results != null) {

                        JSONObject object = new JSONObject(results.toString());
                        if (object.has("status") && object.optString("status").equalsIgnoreCase("0")) {
                            JSONArray array = object.getJSONArray("golfcourse_detail");
                            JSONObject jsonobject = array.getJSONObject(0);
                            String description = jsonobject.optString("description");
                            String address = jsonobject.optString("address");
                            String name = jsonobject.optString("name");
                            String glogo = jsonobject.optString("glogo");
                            String gimage = jsonobject.optString("gimage");
                            email = jsonobject.optString("email");
                            website = jsonobject.optString("website");
                            phone = jsonobject.optString("phone");


                            String totla_url = AppSettings.getInstance(this).getPropertyValue("i_path");
                            String logo_url = totla_url + glogo;
                            String mainimg_url = totla_url + gimage;

                            ((TextView) findViewById(R.id.desc_txt)).setText(Html.fromHtml(description));
                            ((TextView) findViewById(R.id.contact_txt)).setText(Html.fromHtml(address));
                            ((TextView) findViewById(R.id.name_golf)).setText(Html.fromHtml(name));
                            if (website.length() > 0) {
                                ((TextView) findViewById(R.id.contact_web)).setText("Website : " + Html.fromHtml("<u>" + website + "</u>"));
                            } else {
                                ((TextView) findViewById(R.id.contact_web)).setVisibility(View.GONE);
                            }
                            if (phone.length() > 0) {
                                ((TextView) findViewById(R.id.contact_phone)).setText("Phone : " + Html.fromHtml("<u>" + phone + "</u>"));
                            } else {
                                ((TextView) findViewById(R.id.contact_phone)).setVisibility(View.GONE);
                            }
                            if (email.length() > 0) {
                                ((TextView) findViewById(R.id.contact_email)).setText("Email : " + Html.fromHtml("<u>" + email + "</u>"));
                            } else {
                                ((TextView) findViewById(R.id.contact_email)).setVisibility(View.GONE);
                            }
                            findViewById(R.id.contact_email).setOnClickListener(this);
                            findViewById(R.id.contact_phone).setOnClickListener(this);
                            findViewById(R.id.contact_web).setOnClickListener(this);
                            Picasso.with(this).load(logo_url).placeholder(R.mipmap.gfg_location_img).noFade().into(st_image_logo);
                            Picasso.with(this).load(mainimg_url).placeholder(R.mipmap.gfg_location_img).noFade().into(st_main_img);
                            JSONArray offerobject = jsonobject.getJSONArray("offerdetails");
                            if (offerobject.length() == 0) {
                                // showToast("No Offers Found");
                                ((TextView) findViewById(R.id.title_offer)).setVisibility(View.GONE);
                                offer_ll.setVisibility(View.GONE);
                                ((TextView) findViewById(R.id.off_more)).setVisibility(View.GONE);
                            } else {
                                offer_ll.setVisibility(View.VISIBLE);
                                ((TextView) findViewById(R.id.off_more)).setVisibility(View.VISIBLE);
                                ((TextView) findViewById(R.id.title_offer)).setVisibility(View.VISIBLE);
                                for (int j = 0; j < 1; j++) {

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

                                    statListView = (ListView) findViewById(R.id.list_ofers);
                                    statListView.setVisibility(View.VISIBLE);
                                    adapter = new OffersAdapter(this, statesList, this);
                                    statListView.setAdapter(adapter);
                                    ViewCompat.setNestedScrollingEnabled(statListView, true);

                                }
                            }
                            JSONArray spofferobject = jsonobject.getJSONArray("specialdetails");
                            if (spofferobject.length() == 0) {
                                //showToast("No Special Offers Found");
                                sp_offers_ll.setVisibility(View.GONE);
                                ((TextView) findViewById(R.id.title_spoffer)).setVisibility(View.GONE);
                                ((TextView) findViewById(R.id.spoff_viewmore)).setVisibility(View.GONE);
                            } else {
                                sp_offers_ll.setVisibility(View.VISIBLE);
                                ((TextView) findViewById(R.id.title_spoffer)).setVisibility(View.VISIBLE);
                                ((TextView) findViewById(R.id.spoff_viewmore)).setVisibility(View.VISIBLE);
                                for (int j = 0; j < 1; j++) {

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
                                    offerview.setVisibility(View.VISIBLE);
                                    spAdapter = new SpecialOfferAdapter(this, offersModelList);
                                    offerview.setAdapter(spAdapter);
                                    ViewCompat.setNestedScrollingEnabled(offerview, true);

                                }
                            }
                            JSONArray aminitsobj = jsonobject.getJSONArray("amenitiesdetails");
                            if (aminitsobj.length() == 0) {
                                //showToast("No Special Offers Found");
                                aminitis_ll.setVisibility(View.GONE);
                                return;
                            } else {
                                aminitis_ll.setVisibility(View.VISIBLE);
                                for (int k = 0; k < aminitsobj.length(); k++) {

                                    AminitesModel map = new AminitesModel();
                                    JSONObject jsonte = aminitsobj.getJSONObject(k);
                                    String names = jsonte.getString("aname");

                                    map.setName(names);
                                    aminitesModelList.add(map);

                                    aminiteslist = (ListView) findViewById(R.id.list_aminites);
                                    amiAdapter = new AmenitiesAdapter(this, aminitesModelList);
                                    aminiteslist.setAdapter(amiAdapter);
                                    ViewCompat.setNestedScrollingEnabled(aminiteslist, true);
                                }
                            }
                        }


                    }
                    break;
                case 2:
                    if (results != null) {

                        JSONObject object = new JSONObject(results.toString());
                        if (object.has("status") && object.optString("status").equalsIgnoreCase("0")) {
                            //showToast("Swipe to see images/Auto slide");
                            JSONArray array = object.getJSONArray("images_detail");
                            for (int j = 0; j < array.length(); j++) {
                                GolfImageModel map = new GolfImageModel();
                                JSONObject jsonte = array.getJSONObject(j);
                                String sname = jsonte.getString("filename");
                                String edate = jsonte.getString("fileuniquename");

                                map.setGimage(sname);
                                map.setFullname(edate);
                                mvarraylist.add(map);

                            }
                            dialogBox();
                        }
                        //showToast(object.optString("statusdescription"));
                        return;
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

    private void dialogBox() {

        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.dialog_scroll_ll);
        mDialog.getWindow().setGravity(Gravity.NO_GRAVITY);
        mDialog.show();
        final ViewPager mPager = (ViewPager) mDialog.findViewById(R.id.pager);

        mPager.setAdapter(new SlidingImage_Adapter(getApplicationContext(), mvarraylist));


        final float density = getResources().getDisplayMetrics().density;
        //indicator.setRadius(5 * density);
        NUM_PAGES = mvarraylist.size();
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 5000, 5000);

    }

    @Override
    public void onError(String errorCode, int requestType) {
        showToast(errorCode);
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onMessageReedmClicked(int position) {

    }

    @Override
    public void onMessageReedmClicked(String golfId, String offerId) {
        // {"email":"","offerid":"","goflid":""}

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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.off_more:
                Intent offers = new Intent(getApplicationContext(), MoreOffers.class);
                offers.putExtra("gid", gid);
                startActivity(offers);
                break;

            case R.id.spoff_viewmore:
                Intent spoff_viewmore = new Intent(getApplicationContext(), MoreSpecialOffers.class);
                spoff_viewmore.putExtra("gid", gid);
                startActivity(spoff_viewmore);
                break;

            case R.id.contact_email:
                if (email.toString().length() == 0) {
                    showToast("Email Address not found...");
                    return;
                } else {
                    String[] CC = {""};
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("text/html");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                    emailIntent.putExtra(Intent.EXTRA_CC, CC);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Type here message");
                    startActivity(emailIntent);
                }
                break;
            case R.id.contact_phone:
                if (phone.toString().length() == 0) {
                    showToast("Number not found...");
                    return;
                } else {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                    startActivity(intent);
                }
                break;
            case R.id.contact_web:
                if (website.toString().length() == 0) {
                    showToast("Url not found...");
                    return;
                } else {
                    Intent gurl = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
                    try {
                        startActivity(gurl);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(this, "Sorry there is no page.", Toast.LENGTH_SHORT).show();
                    }

                }

                break;
        }

    }
}


