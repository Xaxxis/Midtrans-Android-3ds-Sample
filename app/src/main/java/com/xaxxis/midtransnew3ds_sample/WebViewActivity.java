package com.xaxxis.midtransnew3ds_sample;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {

    private WebView wv;
    private ProgressBar progressBar;

    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled", "ObsoleteSdkInt"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        WebView webView = findViewById(R.id.webview);
        progressBar = findViewById(R.id.pg_bar);

        // TODO: change value client key
        String clientKey = "<your-client-key>";
        String env = getEnv(false);
        String baseUrl = getBaseUrl(false);

        String url3ds = getIntent().getStringExtra("url");

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        } else {
            CookieManager.getInstance().setAcceptCookie(true);
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

        });

        webView.addJavascriptInterface(new WebViewJavaScriptInterface(), "JsBridge");

        String data = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <style> html, body { margin: 0; padding: 0;}  iframe { border: none; width: 100%; height: 100%; position: fixed; left: 0; top: 0;}</style>\n" +
                "</head>\n" +
                "\n" +
                "<body onload=\"view3ds();\">\n" +
                "<iframe name=\"iFrame\"></iframe>\n" +
                "</body>\n" +
                "<script id=\"midtrans-script\" src=\""+baseUrl+"v2/assets/js/midtrans-new-3ds.min.js\"\n" +
                "        data-environment='"+env+"' data-client-key='"+clientKey+"' type=\"text/javascript\"></script>\n" +
                "<script type=\"text/javascript\">\n" +
                "    function view3ds() {\n" +
                "        MidtransNew3ds.authenticate('"+url3ds+"', {\n" +
                "            performAuthentication: function (redirect_url) {\n" +
                "                document.getElementsByName('iFrame')[0].src = redirect_url;\n" +
                "            },\n" +
                "            onSuccess: function (response) {JsBridge.sendMessage(response.status_message);},\n" +
                "            onFailure: function (response) {JsBridge.sendMessage(response.status_message);},\n" +
                "            onPending: function (response) {JsBridge.sendMessage(response.status_message);},\n" +
                "        });\n" +
                "    }\n" +
                "\n" +
                "</script>\n" +
                "</html>";


        webView.loadDataWithBaseURL(baseUrl, data, "text/html", "utf-8", null);
    }

    private class WebViewJavaScriptInterface {
        @android.webkit.JavascriptInterface
        public void sendMessage(String message) {
            Toast.makeText(WebViewActivity.this, message, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private String getBaseUrl(boolean isProduction) {
        return isProduction ? "https://api.midtrans.com/" : "https://api.sandbox.midtrans.com/";
    }

    private String getEnv(boolean isProduction) {
        return isProduction ? "production" : "sandbox";
    }

}