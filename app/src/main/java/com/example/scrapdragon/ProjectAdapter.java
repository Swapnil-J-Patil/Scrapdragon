package com.example.scrapdragon;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

    Context context;
    ArrayList<ProjectModel> list;
    public ProjectAdapter(Context context, ArrayList<ProjectModel> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ProjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectAdapter.ViewHolder holder, int position) {

        ProjectModel projectModel =list.get(position);
        holder.productName.setText(projectModel.getProduct());
        holder.cityName.setText(projectModel.getCity());
        holder.mobileNumber.setText(projectModel.getMobileNumber());
        holder.productdes.setText(projectModel.getProductDescription());
        holder.productprice.setText(projectModel.getProductPrice());
        holder.sellersname.setText(projectModel.getSellersName());

        Picasso.get().load(projectModel.getImageUrl()).placeholder(R.drawable.uploadimg).into(holder.productImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ViewActivity.class);
                intent.putExtra("view_itemimage",projectModel.getImageUrl());
                intent.putExtra("view_itemtvHeadline",projectModel.getProduct());
                intent.putExtra("view_itemprice",projectModel.getProductPrice());
                intent.putExtra("view_itemcity",projectModel.getCity());
                intent.putExtra("view_itemmobile",projectModel.getMobileNumber());
                intent.putExtra("view_sellerName",projectModel.getSellersName());
                intent.putExtra("view_itemtvDescription",projectModel.getProductDescription());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage=itemView.findViewById(R.id.view_itemimage);
            productName=itemView.findViewById(R.id.view_itemtvHeadline);
            cityName=itemView.findViewById(R.id.view_itemcity);
            mobileNumber=itemView.findViewById(R.id.view_itemmobile);
            productdes=itemView.findViewById(R.id.view_itemtvDescription);
            productprice=itemView.findViewById(R.id.view_itemprice);
            sellersname=itemView.findViewById(R.id.view_sellerName);
        }
    }
}
