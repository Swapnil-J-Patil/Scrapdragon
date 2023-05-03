package com.example.scrapdragon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BuyActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<ProjectModel> recyclelist;
    FirebaseDatabase firebaseDatabase;
    SearchView searchinput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        firebaseDatabase=FirebaseDatabase.getInstance();
        recyclerView = findViewById(R.id.recyclerview);
        searchinput=findViewById(R.id.searchinput);
        searchinput.clearFocus();
        recyclelist = new ArrayList<>();

        ProjectAdapter recyclerAdapter=new ProjectAdapter(getApplicationContext(), recyclelist);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(recyclerAdapter);

        firebaseDatabase.getReference().child("Product").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    ProjectModel projectModel=dataSnapshot.getValue(ProjectModel.class);
                    recyclelist.add(projectModel);
                }
                recyclerAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        searchinput.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterData(newText);
                return true;
            }
        });
    }
    private void filterData(String query) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Product");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Convert the data snapshot to a list of your data model class
                List<ProjectModel> data = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ProjectModel item = snapshot.getValue(ProjectModel.class);
                    data.add(item);
                }

                // Filter the data based on the search query
                ArrayList<ProjectModel> filteredData = new ArrayList<>();
                for (ProjectModel item : data) {
                    if (item.getCity().toLowerCase().contains(query.toLowerCase())) {
                        filteredData.add(item);
                    }
                    else if (item.getProduct().toLowerCase().contains(query.toLowerCase())) {
                        filteredData.add(item);
                    }
                }

                // Pass the filtered data to your RecyclerView adapter
                ProjectAdapter adapter = new ProjectAdapter(getApplicationContext(),filteredData);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}