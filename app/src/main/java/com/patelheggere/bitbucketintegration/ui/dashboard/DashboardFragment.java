package com.patelheggere.bitbucketintegration.ui.dashboard;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.patelheggere.bitbucketintegration.R;

public class DashboardFragment extends Fragment {
    private static final String TAG = "DashboardFragment";

    private DashboardViewModel dashboardViewModel;
    private WebView webView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        webView = root.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://oust-saml.oustme.com?idp=http://webmail.exidelifesales.in/adfs/services/trust");
        @SuppressWarnings("unused")
        class MyJavaScriptInterface {
            @JavascriptInterface
            public void processHTML(String html) {
//                here we get string of complete webpage from javascript interface
                if (html.contains("id=\"user_id\"")) {
                    // hideSAMLWebView();
                   // parseHtmlStringtoGetUserId(html);
                }
            }
        }
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.clearCache(true);
                Log.d(TAG, "onPageFinished: ");
                // webView.loadUrl(url);
                if(url.indexOf("/index.jsp")!=-1){
                    webView.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Log.d(TAG, "onReceivedError: "+error.getDescription());
                }
            }
        });
        return root;
    }
}