package com.example.scrapdragon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private  static int SPLASH_SCREEN= 2800;
    private Intent serviceIntent;
    ImageView imageView;
    TextView textView1,textView2;
    Animation top, bottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        imageView=findViewById(R.id.imageView);
        textView1=findViewById(R.id.textView1);

        top= AnimationUtils.loadAnimation(this, R.anim.top);
        bottom= AnimationUtils.loadAnimation(this, R.anim.bottom);

        imageView.setAnimation(top);
        textView1.setAnimation(bottom);
        serviceIntent=new Intent(getApplicationContext(),MyService.class);
        startService(new Intent(getApplicationContext(),MyService.class));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                stopService(new Intent(getApplicationContext(),MyService.class));
                overridePendingTransition(R.anim.up_slide_out,R.anim.down_slide_in);
                finish();
            }
        },SPLASH_SCREEN);
    }
}