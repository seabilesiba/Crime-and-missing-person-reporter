package com.example.crimeandmissingreport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.crimeandmissingreport.adapters.AllMissingAdapter;
import com.example.crimeandmissingreport.adapters.FirstMissingAdapter;
import com.example.crimeandmissingreport.adapters.MissingAdapter;
import com.example.crimeandmissingreport.databinding.ActivityAllMissingBinding;
import com.example.crimeandmissingreport.models.MissingData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllMissingActivity extends AppCompatActivity {

    private ActivityAllMissingBinding binding;
    private List<MissingData> list;
    private FirstMissingAdapter adapter;

    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllMissingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        reference = FirebaseDatabase.getInstance().getReference("AllMissing");


        binding.inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());

            }
        });


        setData();



    }
    private void filter(String text) {

        List<MissingData> filterList = new ArrayList<>();
        for(MissingData items : list){
            if(items.getZipCode().toLowerCase().contains(text.toLowerCase())){
                filterList.add(items);
            }
        }
        adapter.filterList(filterList);
    }
    private void setData() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    MissingData missingData = dataSnapshot.getValue(MissingData.class);
                    list.add(missingData);
                }
                adapter = new FirstMissingAdapter(list,AllMissingActivity.this);
                binding.missingsRecyclerView.setLayoutManager(new LinearLayoutManager(AllMissingActivity.this));
                binding.missingsRecyclerView.setAdapter(adapter);
                binding.missingsRecyclerView.setVisibility(View.VISIBLE);
                binding.pb.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}