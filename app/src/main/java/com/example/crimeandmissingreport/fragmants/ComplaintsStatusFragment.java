package com.example.crimeandmissingreport.fragmants;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.crimeandmissingreport.R;
import com.example.crimeandmissingreport.adapters.ComplaintsAdapter;
import com.example.crimeandmissingreport.models.ComplaintsData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ComplaintsStatusFragment extends Fragment {

    private RecyclerView complaintsRecyclerView;
    private EditText inputSearch;
    private ImageView imgSearch,imgEdit;
    private List<ComplaintsData> list;
    private ComplaintsAdapter adapter;
    private DatabaseReference reference;
    private ProgressBar pd;
    private FirebaseAuth auth;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_complaints_status, container, false);
        complaintsRecyclerView = view.findViewById(R.id.complaintsRecyclerView);
        pd = view.findViewById(R.id.pb);

        inputSearch = view.findViewById(R.id.inputSearch);
        imgSearch = view.findViewById(R.id.imgSearch);
        imgEdit = view.findViewById(R.id.imgEdit);

        reference = FirebaseDatabase.getInstance().getReference("ComplaintsData");
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputSearch.setVisibility(View.VISIBLE);
            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {
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


        return view;
    }
    private void filter(String text) {

        List<ComplaintsData> filterList = new ArrayList<>();
        for(ComplaintsData items : list){
            if(items.getZipCode().toLowerCase().contains(text.toLowerCase())){
                filterList.add(items);
            }
        }
        adapter.filterList(filterList);
    }
    private void setData() {
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    ComplaintsData complaintsData = dataSnapshot.getValue(ComplaintsData.class);
                    list.add(complaintsData);
                }
                adapter = new ComplaintsAdapter(list,getContext());
                complaintsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                complaintsRecyclerView.setAdapter(adapter);
                complaintsRecyclerView.setVisibility(View.VISIBLE);
                pd.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}