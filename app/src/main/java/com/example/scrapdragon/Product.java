package com.example.scrapdragon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Product extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE = 101;
    private ImageView imageViewadd;
    private EditText inputInputName,mobilenumber,cityname,description,price,personName;
    private TextView textViewProgress;
    private ProgressBar progressBar;
    private Button btnUpload;
    Uri imageUri;
    boolean IsImageAdded=false;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    int i=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

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

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Product");
        storageReference= FirebaseStorage.getInstance().getReference().child("ProductImage");

        textViewProgress.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

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
                final String imageName=inputInputName.getText().toString();
                final String city = cityname.getText().toString();
                final String mobile = mobilenumber.getText().toString();
                final String productDescription = description.getText().toString();
                final String productPrice = price.getText().toString();
                final String customerName = personName.getText().toString();

                if(imageName.isEmpty())
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
                else if(IsImageAdded!=false && imageName!=null && customerName!=null && city!=null && mobile!=null && productDescription!=null && productPrice!=null)
                {
                    uploadImage(imageName,city,mobile,productDescription,productPrice,customerName);
                }
                else
                {
                    Toast.makeText(Product.this, "Select an image of Product", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadImage(String imageName,String cityName,String mobileNum,String productdes,String productprice,String customername)
    {
        textViewProgress.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        final String key=databaseReference.push().getKey();
        storageReference.child(key+ ".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.child(key+ ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String email = user.getEmail();
                            String emailnew=email.replace(".","A");
                            Date currentTime = Calendar.getInstance().getTime();

                            HashMap hashMap=new HashMap();
                            hashMap.put("Product",imageName);
                            hashMap.put("MobileNumber",mobileNum);
                            hashMap.put("City",cityName);
                            hashMap.put("ProductDescription",productdes);
                            hashMap.put("ProductPrice",productprice);
                            hashMap.put("SellersName",customername);

                            hashMap.put("ImageUrl",uri.toString());

                            databaseReference.child(emailnew+" : "+currentTime).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    textViewProgress.setVisibility(View.GONE);
                                    progressBar.setVisibility(View.GONE);
                                    inputInputName.setText("");
                                    cityname.setText("");
                                    mobilenumber.setText("");
                                    description.setText("");
                                    price.setText("");
                                    personName.setText("");
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