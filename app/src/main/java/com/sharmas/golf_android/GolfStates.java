package com.sharmas.golf_android;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sharmas.golf_android.adapter.LocationAdapter;
import com.sharmas.golf_android.adapter.StatesAdapter;
import com.sharmas.golf_android.callbacks.IItemHandler;
import com.sharmas.golf_android.common.AppPreferences;
import com.sharmas.golf_android.common.AppSettings;
import com.sharmas.golf_android.models.GolfStatesModel;
import com.sharmas.golf_android.network.HTTPTask;
import com.sharmas.golf_android.network.HTTPostJson;
import com.sharmas.golf_android.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Created by Admin on 2017-03-24.
 */

public class GolfStates extends AppCompatActivity implements IItemHandler {

    private StatesAdapter adapter;
    private LocationAdapter ladapter;
    private List<GolfStatesModel> statesList;
    private ListView statListView;
    private SwipeRefreshLayout mSwipeRefreshLayout = null;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private Dialog mDilaog = null;
    private EditText current_ed, current_ed_loc;
    private ArrayList<String> list = null;

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

        findViewById(R.id.title_info).setVisibility(View.VISIBLE);
        statListView = (ListView) findViewById(R.id.states_list);
        findViewById(R.id.liner_states).setVisibility(View.VISIBLE);
        current_ed = (EditText) findViewById(R.id.get_laocation_text);
        current_ed.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    statListView.setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.no_data)).setVisibility(View.GONE);
                    getData();
                    Utils.dismissProgress();
                }
            }
        });
        current_ed_loc = (AutoCompleteTextView) findViewById(R.id.get_location);
        current_ed_loc.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    statListView.setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.no_data)).setVisibility(View.GONE);
                    getData();
                    Utils.dismissProgress();
                }
            }
        });
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimaryDark,
                R.color.colorPrimaryDark, R.color.colorPrimaryDark, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                getData();
                statListView.setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.no_data)).setVisibility(View.GONE);
                Utils.dismissProgress();

            }
        });
        getData();
        getSearchlocation();
        if (!checkPermission()) {
            requestPermission();
            return;
        } else {
            getMyCurrentLocation();
        }
        current_ed.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null) || actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_GO) {
                    searchLocations(1);
                }
                return true;
            }
        });
        current_ed_loc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null) || actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_GO) {
                    searchLocations(2);
                }
                return true;
            }
        });
    }


    private void searchLocations(int type) {

        try {


            String url = AppSettings.getInstance(this).getPropertyValue("golf_search");
            JSONObject object = new JSONObject();
            if (type == 1) {
                current_ed.clearFocus();
                InputMethodManager in = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(current_ed.getWindowToken(), 0);
                object.put("location", current_ed.getText().toString().trim());
            } else {
                current_ed_loc.clearFocus();
                InputMethodManager in = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(current_ed_loc.getWindowToken(), 0);
                object.put("location", current_ed_loc.getText().toString().trim());
            }
            HTTPostJson post = new HTTPostJson(this, this, object.toString(), 3);
            post.setContentType("application/json");
            post.execute(url, "");
            Utils.showProgress("Searching...", this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getData() {
        try {

            String url = AppSettings.getInstance(this).getPropertyValue("states_golf");
            HTTPTask task = new HTTPTask(this, this);
            task.userRequest(getString(R.string.loading), 1, url);
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
                GolfStates.this.finish();
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
                    Utils.dismissProgress();
                    if (results != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mSwipeRefreshLayout.setEnabled(true);
                        JSONObject object = new JSONObject(results.toString());
                        if (object.has("status") && object.optString("status").equalsIgnoreCase("0")) {
                            JSONArray array = object.getJSONArray("state_detail");
                            for (int i = 0; i < array.length(); i++) {
                                GolfStatesModel map = new GolfStatesModel();
                                JSONObject jsonobject = array.getJSONObject(i);
                                String sname = jsonobject.getString("state");
                                String sid = jsonobject.getString("stateid");

                                map.setStateName(sname);
                                map.setStateId(sid);
                                statesList.add(map);
                            }
                            adapter = new StatesAdapter(this, statesList);
                            statListView.setAdapter(adapter);
                        }
                        if (object.has("status") && object.optString("status").equalsIgnoreCase("1")) {
                            ((TextView) findViewById(R.id.no_data)).setVisibility(View.VISIBLE);
                            ((TextView) findViewById(R.id.no_data)).setText(object.optString("statusdescription"));
                            return;
                        }


                    }
                    break;
                case 2:
                    parseToSuggestion((String) results, requestType);
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                            android.R.layout.simple_expandable_list_item_1, list);
                    ((AutoCompleteTextView) findViewById(R.id.get_location)).setAdapter(adapter1);
                    ((AutoCompleteTextView) findViewById(R.id.get_location)).setThreshold(1);
                    break;
                case 3:
                    Utils.dismissProgress();
                    if (results != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mSwipeRefreshLayout.setEnabled(false);
                        JSONObject object = new JSONObject(results.toString());
                        if (object.has("status") && object.optString("status").equalsIgnoreCase("0")) {
                            JSONArray array = object.getJSONArray("golfcourse_detail");
                            for (int i = 0; i < array.length(); i++) {
                                GolfStatesModel map = new GolfStatesModel();
                                JSONObject jsonobject = array.getJSONObject(i);
                                String sname = jsonobject.getString("name");
                                String sid = jsonobject.getString("golfid");

                                map.setStateName(sname);
                                map.setGolfId(sid);
                                statesList.add(map);
                            }

                            ladapter = new LocationAdapter(this, statesList);
                            statListView.setAdapter(ladapter);
                        }
                        if (object.has("status") && object.optString("status").equalsIgnoreCase("1")) {
                            ((TextView) findViewById(R.id.no_data)).setVisibility(View.VISIBLE);
                            statListView.setVisibility(View.GONE);
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

    public void turnGPSOn() {
        try {

            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);


            if (!provider.contains("gps")) { //if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                sendBroadcast(poke);
            }
        } catch (Exception e) {

        }
    }

    // Method to turn off the GPS
    public void turnGPSOff() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (provider.contains("gps")) { //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);

        }
    }

    public void getMyCurrentLocation() {


        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locListener = new MyLocationListener();


        try {
            gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        //don't start listeners if no provider is enabled
        //if(!gps_enabled && !network_enabled)
        //return false;

        if (gps_enabled) {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);

        }


        if (gps_enabled) {
            location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        }


        if (network_enabled && location == null) {
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);

        }


        if (network_enabled && location == null) {
            location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        }

        if (location != null) {

            MyLat = location.getLatitude();
            MyLong = location.getLongitude();


        } else {
            Location loc = getLastKnownLocation(this);
            if (loc != null) {

                MyLat = loc.getLatitude();
                MyLong = loc.getLongitude();


            }
        }
        locManager.removeUpdates(locListener); // removes the periodic updates from location listener to //avoid battery drainage. If you want to get location at the periodic intervals call this method using //pending intent.

        try {
// Getting address from found locations.
            Geocoder geocoder;

            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(MyLat, MyLong, 1);

            StateName = addresses.get(0).getAdminArea();
            CityName = addresses.get(0).getLocality();
            pincode = addresses.get(0).getPostalCode();
            CountryName = addresses.get(0).getCountryName();
            // you can get more details other than this . like country code, state code, etc.


//            System.out.println(" StateName " + StateName);
//            System.out.println(" CityName " + CityName);
//            System.out.println(" CountryName " + CountryName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (CityName == null) {
            current_ed.setText(Html.fromHtml(StateName));
        } else {
            current_ed.setText(Html.fromHtml(CityName));
        }

    }

    // Location listener class. to get location.
    public class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location location) {
            if (location != null) {
            }
        }

        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }

    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    Location location;

    Double MyLat, MyLong;
    String CityName = "";
    String StateName = "";
    String CountryName = "";
    String pincode = "";

// below method to get the last remembered location. because we don't get locations all the times .At some instances we are unable to get the location from GPS. so at that moment it will show us the last stored location.

    public Location getLastKnownLocation(Context context) {

        Location location = null;
        LocationManager locationmanager = (LocationManager) context.getSystemService("location");
        List list = locationmanager.getAllProviders();
        boolean i = false;
        Iterator iterator = list.iterator();
        do {
            //System.out.println("---------------------------------------------------------------------");
            if (!iterator.hasNext())
                break;
            String s = (String) iterator.next();
            //if(i != 0 && !locationmanager.isProviderEnabled(s))
            if (i != false && !locationmanager.isProviderEnabled(s))
                continue;
            // System.out.println("provider ===> "+s);
            Location location1 = locationmanager.getLastKnownLocation(s);
            if (location1 == null)
                continue;
            if (location != null) {
                //System.out.println("location ===> "+location);
                //System.out.println("location1 ===> "+location);
                float f = location.getAccuracy();
                float f1 = location1.getAccuracy();
                if (f >= f1) {
                    long l = location1.getTime();
                    long l1 = location.getTime();
                    if (l - l1 <= 600000L)
                        continue;
                }
            }
            location = location1;
            // System.out.println("location  out ===> "+location);
            //System.out.println("location1 out===> "+location);
            i = locationmanager.isProviderEnabled(s);
            // System.out.println("---------------------------------------------------------------------");
        } while (true);
        return location;
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean corelocation = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && corelocation) {
                        turnGPSOn();
                        getMyCurrentLocation();
                        // Toast.make(this, "Permission Granted, Now you can access location data and camera.", Toast.LENGTH_LONG).show();
                    } else {

                        //Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public void getSearchlocation() {

        try {

            String url = AppSettings.getInstance(this).getPropertyValue("laocations");
            HTTPTask task = new HTTPTask(this, this);
            task.disableProgress();
            task.userRequest(getString(R.string.loading), 2, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseToSuggestion(String results, int requestType) throws Exception {
        if (results != null && results.length() > 0) {
            JSONObject object = new JSONObject(results.toString());
            if (object.has("status") && object.optString("status").equalsIgnoreCase("0")) {
                JSONArray array = object.getJSONArray("location_detail");
                if (array != null && array.length() > 0) {
                    list = new ArrayList<String>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jObject = array.getJSONObject(i);
                        String useremails = jObject.optString("location");
                        String locationid = jObject.optString("locationid");
                        useremails = useremails.trim();
                        if (useremails.length() > 0) {
                            list.add(useremails);

                        }
                    }

                }
            }
        }
    }

}
