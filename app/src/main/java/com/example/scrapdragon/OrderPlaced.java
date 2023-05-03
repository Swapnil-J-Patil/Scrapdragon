package com.example.scrapdragon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class OrderPlaced extends AppCompatActivity {
    private  static int SPLASH_SCREEN= 2800;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(OrderPlaced.this, SellActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.up_slide_out,R.anim.down_slide_in);
                finish();
            }
        },SPLASH_SCREEN);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(OrderPlaced.this, "Your Order Has Been Posted", Toast.LENGTH_SHORT).show();
                finish();
            }
        },SPLASH_SCREEN);
    }
}