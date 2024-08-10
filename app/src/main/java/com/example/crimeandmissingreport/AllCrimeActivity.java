package com.example.crimeandmissingreport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.crimeandmissingreport.adapters.AllCrimesAdapter;
import com.example.crimeandmissingreport.adapters.CrimeAdapter;
import com.example.crimeandmissingreport.adapters.FirstCrimeAdapter;
import com.example.crimeandmissingreport.adapters.MissingAdapter;
import com.example.crimeandmissingreport.databinding.ActivityAllCrimeBinding;
import com.example.crimeandmissingreport.databinding.ActivityAllMissingBinding;
import com.example.crimeandmissingreport.models.CrimeData;
import com.example.crimeandmissingreport.models.MissingData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllCrimeActivity extends AppCompatActivity {

    private ActivityAllCrimeBinding binding;
    private List<CrimeData> list;
    private FirstCrimeAdapter adapter;

    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllCrimeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        reference = FirebaseDatabase.getInstance().getReference("AllCrime");


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

        List<CrimeData> filterList = new ArrayList<>();
        for(CrimeData items : list){
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
                    CrimeData crimeData = dataSnapshot.getValue(CrimeData.class);
                    list.add(crimeData);
                }
                adapter = new FirstCrimeAdapter(list,AllCrimeActivity.this);
                binding.crimesRecyclerView.setLayoutManager(new LinearLayoutManager(AllCrimeActivity.this));
                binding.crimesRecyclerView.setAdapter(adapter);
                binding.crimesRecyclerView.setVisibility(View.VISIBLE);
                binding.pb.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}