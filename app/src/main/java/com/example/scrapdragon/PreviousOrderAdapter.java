package com.example.scrapdragon;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class PreviousOrderAdapter extends RecyclerView.Adapter<PreviousOrderAdapter.ViewHolder> {
    Context context;
    ArrayList<ProjectModel> list;
    public PreviousOrderAdapter(Context context, ArrayList<ProjectModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PreviousOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_previous_order,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PreviousOrderAdapter.ViewHolder holder, int position) {

        ProjectModel projectModel =list.get(position);
        holder.productName.setText(projectModel.getProduct());
        holder.cityName.setText(projectModel.getCity());
        holder.mobileNumber.setText(projectModel.getMobileNumber());
        holder.productdes.setText(projectModel.getProductDescription());
        holder.productprice.setText(projectModel.getProductPrice());
        holder.sellersname.setText(projectModel.getSellersName());

        Picasso.get().load(projectModel.getImageUrl()).placeholder(R.drawable.uploadimg).into(holder.productImage);

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.productName.getContext());
                builder.setTitle("Are You Sure?");
                builder.setMessage("You want to cancel the order?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Product").child(projectModel.getNodekey());
                        StorageReference storageReference= FirebaseStorage.getInstance().getReferenceFromUrl(projectModel.getImageUrl());
                        databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Your Order Has Been Cancelled", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(context,SellActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                        ((Activity)context).finish();
                                    }
                                });
                            }
                        });
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView productImage;
        TextView productName,cityName,mobileNumber,productdes,productprice,sellersname;
        Button cancel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage=itemView.findViewById(R.id.itemprevious_productimage);
            productName=itemView.findViewById(R.id.itemprevious_productname);
            cityName=itemView.findViewById(R.id.itemprevious_city);
            mobileNumber=itemView.findViewById(R.id.itemprevious_sellersmobile);
            productdes=itemView.findViewById(R.id.itemprevious_previousdescription);
            productprice=itemView.findViewById(R.id.itemprevious_price);
            sellersname=itemView.findViewById(R.id.itemprevious_sellersname);
            cancel=itemView.findViewById(R.id.itemprevious_btn_cancel);
        }
    }

}
