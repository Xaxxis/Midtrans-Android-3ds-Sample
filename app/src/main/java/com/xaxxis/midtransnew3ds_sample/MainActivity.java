package com.xaxxis.midtransnew3ds_sample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button btn3dsPage;
    private EditText tv_3dsUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindView();
        btn3dsPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webViewActivity = new Intent(MainActivity.this, WebViewActivity.class);
                webViewActivity.putExtra("url", tv_3dsUrl.getText().toString());
                startActivity(webViewActivity);
            }
        });
    }

    protected void bindView() {
        btn3dsPage = findViewById(R.id.btn_3dsPage);
        tv_3dsUrl = findViewById(R.id.tv_3dsUrl);
    }



}