package com.example.scrapdragon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class SellActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE = 101;
    private ImageView imageViewadd,cart;
    private EditText inputInputName,mobilenumber,cityname,description,price,personName,emailId;
    private TextView textViewProgress;
    private ProgressBar progressBar;
    private Button btnUpload;
    Uri imageUri;
    boolean IsImageAdded=false;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        imageViewadd=findViewById(R.id.imageViewAdd);
        inputInputName=findViewById(R.id.inputName);
        textViewProgress=findViewById(R.id.textviewProgress);
        progressBar=findViewById(R.id.progressbar);
        btnUpload=findViewById(R.id.btn_upload);
        mobilenumber=findViewById(R.id.inputmobnumber);
        cityname=findViewById(R.id.cityname);
        description=findViewById(R.id.inputdescription);
        price=findViewById(R.id.inputexpectedprice);
        personName=findViewById(R.id.inputcustomername);
        emailId=findViewById(R.id.email_ID);
        cart=findViewById(R.id.cart);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Product");
        storageReference= FirebaseStorage.getInstance().getReference().child("ProductImage");

        textViewProgress.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PreviousOrders.class);
                overridePendingTransition(R.anim.left_slide_out, R.anim.right_slide_in);
                startActivity(intent);
                finish();
            }
        });
        imageViewadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODE_IMAGE);
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String productName=inputInputName.getText().toString();
                final String city = cityname.getText().toString();
                final String mobile = mobilenumber.getText().toString();
                final String productDescription = description.getText().toString();
                final String productPrice = price.getText().toString();
                final String customerName = personName.getText().toString();
                final String emailID = emailId.getText().toString();


                SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("customer", customerName);
                editor.putString("mobileNum", mobile);
                editor.putString("city", city);
                editor.putString("email", emailID);
                editor.apply();

                if(productName.isEmpty())
                {
                    inputInputName.setError("Please Enter Product details");
                }
                else if(city.isEmpty())
                {
                    cityname.setError("Please Enter City Name");
                }
                else if(mobile.isEmpty())
                {
                    mobilenumber.setError("Please Enter Mobile Number");
                }
                else if(productDescription.isEmpty())
                {
                    description.setError("Please Enter Product Description");
                }else if(productPrice.isEmpty())
                {
                    mobilenumber.setError("Please Enter Expected Product Price");
                }
                else if(customerName.isEmpty())
                {
                    personName.setError("Please Enter Your Name");
                }
                else if(emailID.isEmpty())
                {
                    emailId.setError("Please Enter Your email ID");
                }
                else if(IsImageAdded!=false && productName!=null && customerName!=null && city!=null && mobile!=null && productDescription!=null && productPrice!=null && emailID!=null)
                {
                    uploadImage(productName,city,mobile,productDescription,productPrice,customerName,emailID);
                }
                else
                {
                    Toast.makeText(SellActivity.this, "Select an image of Product", Toast.LENGTH_SHORT).show();
                }
            }
        });
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String customer = preferences.getString("customer", "");
        String city = preferences.getString("city", "");
        String mobileNum = preferences.getString("mobileNum", "");
        String email = preferences.getString("email", "");

        cityname.setText(city);
        mobilenumber.setText(mobileNum);
        personName.setText(customer);
        emailId.setText(email);
    }

    private void uploadImage(String imageName,String cityName,String mobileNum,String productdes,String productprice,String customername,String emailID)
    {
        textViewProgress.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String email = user.getEmail();
        String emailnew=email.replace(".","A");
        Date currentTime = Calendar.getInstance().getTime();
        String childkeystorage=emailnew+" : "+currentTime+ ".jpg";
        storageReference.child(childkeystorage).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.child(childkeystorage).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String email = user.getEmail();
                            String emailnew=email.replace(".","A");
                            Date currentTime = Calendar.getInstance().getTime();
                            String childkey=emailnew+" : "+currentTime;

                            HashMap hashMap=new HashMap();
                            hashMap.put("Product",imageName);
                            hashMap.put("MobileNumber",mobileNum);
                            hashMap.put("City",cityName);
                            hashMap.put("ProductDescription",productdes);
                            hashMap.put("ProductPrice",productprice);
                            hashMap.put("SellersName",customername);
                            hashMap.put("SellersEmail",emailID);
                            hashMap.put("nodekey",childkey);
                            hashMap.put("ImageUrl",uri.toString());

                            databaseReference.child(childkey).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    textViewProgress.setVisibility(View.GONE);
                                    progressBar.setVisibility(View.GONE);

                                    inputInputName.setText("");
                                    description.setText("");
                                    price.setText("");
                                    imageViewadd.setImageResource(R.drawable.uploadimg);
                                    startActivity(new Intent(getApplicationContext(),OrderPlaced.class));
                                    overridePendingTransition(R.anim.left_slide_out, R.anim.right_slide_in);
                                    finish();
                                }
                            });
                        }else {}
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress= (snapshot.getBytesTransferred()*100)/snapshot.getTotalByteCount();
                progressBar.setProgress((int) progress);
                textViewProgress.setText(progress +"%");
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_IMAGE && data!=null)
        {
            imageUri=data.getData();
            IsImageAdded=true;
            imageViewadd.setImageURI(imageUri);
        }
    }

}