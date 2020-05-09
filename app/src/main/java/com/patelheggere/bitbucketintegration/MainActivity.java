package com.patelheggere.bitbucketintegration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.oustme.oustsdk.activity.cordovacall.CordovaCallActivity;
import com.oustme.oustsdk.interfaces.common.OustLoginCallBack;
import com.oustme.oustsdk.launcher.OustAuthData;
import com.oustme.oustsdk.launcher.OustExceptions.OustException;
import com.oustme.oustsdk.launcher.OustLauncher;
import com.oustme.oustsdk.launcher.OustNotificationConfig;
import com.oustme.oustsdk.launcher.PushNotificationType;

import java.util.Map;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

import static com.oustme.oustsdk.tools.OustSdkTools.formatMilliinFormat;


public class MainActivity extends AppCompatActivity implements OustLoginCallBack {
    private static final String TAG = "MainActivity";
    private Button mOustButton;
    private TextView mTextViewProgress;
    private TextView textViewHtml;
    private EditText username, orgid;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: MAx:"+getMaxTextureSize());

        textViewHtml = findViewById(R.id.textViewHtml);
        textViewHtml.setText("");
        username = findViewById(R.id.userid);
        orgid = findViewById(R.id.orgid);
        webView = findViewById(R.id.webView);
    /*    webView.getSettings().setJavaScriptEnabled(true);
        @SuppressWarnings("unused")
        class MyJavaScriptInterface {
            @JavascriptInterface
            public void processHTML(String html) {
//                here we get string of complete webpage from javascript interface
                if (html.contains("id=\"user_id\"")) {
                    // hideSAMLWebView();
                    //parseHtmlStringtoGetUserId(html);
                }
            }
        }
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        webView.loadUrl("https://oust-saml.oustme.com?idp=http://webmail.exidelifesales.in/adfs/services/trust");
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
        });*/
        Log.d(TAG, "onCreate milliformat: "+formatMilliinFormat(2953));
        mOustButton = findViewById(R.id.oustButton);
        mTextViewProgress = findViewById(R.id.progress);
        Intent intent = new Intent(MainActivity.this, CordovaCallActivity.class);
       mOustButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OustAuthData oustAuthData = new OustAuthData();
                String org = orgid.getText().toString().trim();
                oustAuthData.setOrgId(org);
                oustAuthData.setUsername(username.getText().toString());
                OustNotificationConfig notificationConfig = new OustNotificationConfig();
                notificationConfig.setPushNotificationType(PushNotificationType.FCM);
                notificationConfig.setToken("your FCM Token");
                notificationConfig.setServerKey("Your server Key");
                try {
                    OustLauncher.getInstance().launch(MainActivity.this, oustAuthData, notificationConfig, MainActivity.this);
                } catch (OustException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public static int getMaxTextureSize() {
        // Safe minimum default size
        final int IMAGE_MAX_BITMAP_DIMENSION = 2048;

        // Get EGL Display
        EGL10 egl = (EGL10) EGLContext.getEGL();
        EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

        // Initialise
        int[] version = new int[2];
        egl.eglInitialize(display, version);

        // Query total number of configurations
        int[] totalConfigurations = new int[1];
        egl.eglGetConfigs(display, null, 0, totalConfigurations);

        // Query actual list configurations
        EGLConfig[] configurationsList = new EGLConfig[totalConfigurations[0]];
        egl.eglGetConfigs(display, configurationsList, totalConfigurations[0], totalConfigurations);

        int[] textureSize = new int[1];
        int maximumTextureSize = 0;

        // Iterate through all the configurations to located the maximum texture size
        for (int i = 0; i < totalConfigurations[0]; i++) {
            // Only need to check for width since opengl textures are always squared
            egl.eglGetConfigAttrib(display, configurationsList[i], EGL10.EGL_MAX_PBUFFER_WIDTH, textureSize);

            // Keep track of the maximum texture size
            if (maximumTextureSize < textureSize[0])
                maximumTextureSize = textureSize[0];
        }

        // Release
        egl.eglTerminate(display);

        int heightW = new Canvas().getMaximumBitmapHeight();
        int widthW = new Canvas().getMaximumBitmapWidth();

        Log.d(TAG, "getMaxTextureSize: "+heightW+" width:"+widthW);


        // Return largest texture size found, or default
        return Math.max(maximumTextureSize, IMAGE_MAX_BITMAP_DIMENSION);
    }

    @Override
    public void onStartDownloadingResourses() {

    }

    @Override
    public void onProgressChanged(int i) {
        Log.d(TAG, "onProgressChanged: "+i);
        mTextViewProgress.setText("progess: "+i);
    }

    @Override
    public void onError(String s) {
        Log.d(TAG, "onError: "+s);
    }


    @Override
    public void onLoginError(String s) {

    }

    @Override
    public void onLoginProcessStart() {

    }

    @Override
    public void onLoginSuccess(boolean b) {

    }

    @Override
    public void onNetworkError() {


    }

}
