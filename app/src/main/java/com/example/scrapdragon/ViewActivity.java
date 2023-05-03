package com.example.scrapdragon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ViewActivity extends AppCompatActivity {
private ImageView imageView;
Button btn_contact;
TextView productName,cityName,mobileNumber,productdes,productprice,sellersname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        imageView=findViewById(R.id.itemprevious_productimage);
        productName=findViewById(R.id.itemprevious_productname);
        cityName=findViewById(R.id.itemprevious_city);
        mobileNumber=findViewById(R.id.itemprevious_sellersmobile);
        productdes=findViewById(R.id.itemprevious_previousdescription);
        productprice=findViewById(R.id.itemprevious_price);
        sellersname=findViewById(R.id.itemprevious_sellersname);
        btn_contact=findViewById(R.id.itemprevious_btn_cancel);

        Picasso.get().load(getIntent().getStringExtra("view_itemimage"))
                .placeholder(R.drawable.uploadimg)
                .into(imageView);
        productName.setText(getIntent().getStringExtra("view_itemtvHeadline"));
        cityName.setText(getIntent().getStringExtra("view_itemcity"));
        mobileNumber.setText(getIntent().getStringExtra("view_itemmobile"));
        productdes.setText(getIntent().getStringExtra("view_itemtvDescription"));
        productprice.setText(getIntent().getStringExtra("view_itemprice"));
        sellersname.setText(getIntent().getStringExtra("view_sellerName"));

        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Mobileno = mobileNumber.getText().toString();
                setClickToChat(view,Mobileno);
            }
        });
    }
    public static void setClickToChat(View v,String toNumber){
        String url = "https://api.whatsapp.com/send?phone=" + toNumber;
        try {
            PackageManager pm = v.getContext().getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            v.getContext().startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }
}