package com.example.abhishek.website;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity implements ViewTreeObserver.OnScrollChangedListener {

    private WebView mWebView;
    ProgressBar progressBar;
    TextView t, l;
    SwipeRefreshLayout sp, sp2;
    ImageView ii;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = (WebView) findViewById(R.id.activity_main_webview);
        t = (TextView) findViewById(R.id.textView);
        l = (TextView) findViewById(R.id.LoadingText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        ii = (ImageView) findViewById(R.id.imageview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        mWebView.loadUrl(getString (R.string.url));
        mWebView.setWebViewClient(new HelloWebViewClient());

        sp = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        sp2 = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout2);
        sp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mWebView.reload();
                sp.setRefreshing(false);

            }
        });
        sp2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mWebView.reload();
                sp2.setRefreshing(false);
            }
        });
        sp2.setEnabled(false);

    }

    @Override
    public void onScrollChanged() {
        if (mWebView.getScrollY() == 0) {
            sp.setEnabled(true);
        } else {
            sp.setEnabled(false);
        }
    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            ii.setVisibility(View.INVISIBLE);
            sp2.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            t.setVisibility(View.VISIBLE);
            l.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.INVISIBLE);
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            ii.setVisibility(View.VISIBLE);
            sp2.setEnabled(true);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            webView.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(view.GONE);
            t.setVisibility(view.GONE);
            l.setText(getString (R.string.loading));
            l.setVisibility(view.GONE);
            mWebView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("Exit");
        alertDialogBuilder.setMessage("Do you really want to exit?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
