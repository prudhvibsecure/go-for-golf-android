package com.sharmas.golf_android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.sharmas.golf_android.common.AppSettings;

import org.json.JSONException;

/**
 * Created by user on 3/15/2017.
 */

public class Registration extends AppCompatActivity implements View.OnClickListener {

    private WebView webview = null;
    private WebSettings webSettings = null;
    private String callbackURL = "";

    private Toolbar toolbar = null;

    private ContainerScriptInterface jsInterface = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//
//        String title = getString(R.string.register);
//        toolbar.setTitleTextColor(Color.WHITE);
//        toolbar.setTitle(title);
//        getSupportActionBar().setTitle(title);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setResult(RESULT_CANCELED);
//                close();
//            }
//        });

        try {

            String link = AppSettings.getInstance(this).getPropertyValue("webregister");
            init(link);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void init(String url) {

        jsInterface = new ContainerScriptInterface();

        webview = (WebView) findViewById(R.id.register_web);

        webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webview.addJavascriptInterface(jsInterface, "jsInterface");
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);

        webview.setWebViewClient(new MyWebViewClient());
        webview.setWebChromeClient(new MyWebChromeClient());

        webview.requestFocus(View.FOCUS_DOWN);
        webview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });

        webview.loadUrl(url);
    }

    /**
     * (non-Javadoc)
     *
     * @see WebView#onKeyDown(int, KeyEvent)
     */
    // @Override
    // public boolean onKeyDown(int keyCode, KeyEvent event) {
    // return false;
    // }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == 4) {

        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * MyWebChromeClient - class which extend WebChromeClient for handle page
     * progress.
     *
     * @author shadab
     */
    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {

            // Log.e("-=-=-=-=-=-", newProgress + "");

            if (newProgress == 5)
                findViewById(R.id.pb_payment).setVisibility(View.VISIBLE);

            if (newProgress >= 95) {
                findViewById(R.id.pb_payment).setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }
    }

    /**
     * MyWebViewClient - class which extends WebViewClient to handle internal
     * URL.
     *
     * @author prudhvi
     */
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            findViewById(R.id.pb_payment).setVisibility(View.GONE);
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    }

    private void paymentStatus(int id) {
        switch (id) {
            case 0:
                Toast.makeText(this, "PAYMENT PENDING", Toast.LENGTH_LONG).show();
                // setResult(Constants.PAYMENT_PENDING);
                close();
                break;
            case 1:
                Toast.makeText(this, "PAYMENT SUCCESSFULLY DONE", Toast.LENGTH_LONG).show();
                // setResult(Constants.PAYMENT_SUCCESS);
                close();
                break;
            case 2:
                Toast.makeText(this, "PAYMENT FAILED", Toast.LENGTH_LONG).show();
                // setResult(Constants.PAYMENT_FAILED);
                close();
                break;
        }
    }

    /**
     * close - When payment is done this method is called to release the
     * resource.
     */
    private void close() {
        if (webview != null) {
            webview.stopLoading();
            webview.setBackgroundDrawable(null);
            webview.clearFormData();
            webview.clearHistory();
            webview.clearMatches();
            webview.clearCache(true);
            webview.clearSslPreferences();
            webview = null;
        }

        finish();
    }

    public class ContainerScriptInterface {

        public ContainerScriptInterface() {
        }

        @JavascriptInterface
        public void exec(String service, String action, String arg) throws JSONException {

            try {
                showToast(service + "-----" + action + " -------- " + arg);
                // Log.e("--------------", "service : " + service + " :::
                // action:" + action + " ::::: arg:" + arg);

                showOkMessage(getString(R.string.payment), arg + "", action);

            } catch (Throwable e) {
                showToast(service + "-----" + action + " -------- " + arg);
                e.printStackTrace();

            }
        }

    }

    public void showToast(String value) {
        // Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            default:
                break;
        }
    }

    public void showOkMessage(String title, String message, String action) {

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        bundle.putString("action", action);

        AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
        builder.setTitle("Registration" + " " + title)
                .setMessage(Html.fromHtml(message))
                .setCancelable(false).setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    // if the user agrees to upgrade
                    public void onClick(DialogInterface dialog, int id) {

                        Intent st = new Intent(getApplicationContext(), Golf_Landing.class);
                        startActivity(st);
                        finish();


                    }
                });

        builder.create().show();

        // MessageDialog.newInstance(bundle).show(WebRegister.this.getSupportFragmentManager(), "dialog");

    }

}
