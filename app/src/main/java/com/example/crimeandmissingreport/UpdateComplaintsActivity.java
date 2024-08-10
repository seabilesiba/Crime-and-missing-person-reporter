package com.example.crimeandmissingreport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.crimeandmissingreport.databinding.ActivityUpdateComplaintsBinding;
import com.example.crimeandmissingreport.models.ComplaintsData;
import com.example.crimeandmissingreport.models.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class UpdateComplaintsActivity extends AppCompatActivity {

    private DatabaseReference reference;
    private String compID,userID,address,city,zipCode,subject,complain;
    private ActivityUpdateComplaintsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateComplaintsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        reference = FirebaseDatabase.getInstance().getReference("ComplaintsData");

        compID = getIntent().getStringExtra("compID");
        userID = getIntent().getStringExtra("userID");
        address = getIntent().getStringExtra("address");
        city = getIntent().getStringExtra("city");
        zipCode = getIntent().getStringExtra("zipCode");
        subject = getIntent().getStringExtra("subject");
        complain = getIntent().getStringExtra("complain");

        binding.inputAddress.setText(address);
        binding.inputCity.setText(city);
        binding.inputZipCode.setText(zipCode);
        binding.inputSubject.setText(subject);
        binding.inputComplaints.setText(complain);


        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (isValidDetails()){
                   UpdateComplaints();
               }
            }
        });
        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteComplaints();
            }


        });



    }
    private void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void deleteComplaints() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateComplaintsActivity.this);
        builder.setMessage("Do you really want to delete your Complain").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                reference.child(userID).child(compID).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(UpdateComplaintsActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(UpdateComplaintsActivity.this,MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(UpdateComplaintsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(UpdateComplaintsActivity.this, "Something went wrong "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private Boolean isValidDetails() {

        // private String compID,userID,address,city,zipCode,subject,complain;
        address = binding.inputAddress.getText().toString().trim();
        city = binding.inputCity.getText().toString().trim();
        zipCode = binding.inputZipCode.getText().toString().trim();
        subject = binding.inputSubject.getText().toString().trim();
        complain = binding.inputComplaints.getText().toString().trim();



        if (address.isEmpty()) {
            showMessage("Please enter address");
            return false;
        } else if (city.isEmpty()) {
            showMessage("Please enter city");
            return false;
        } else if (zipCode.isEmpty()) {
            showMessage("Please enter zip code");
            return false;
        } else if (subject.isEmpty()) {
            showMessage("Please enter your subject");
            return false;
        } else if (complain.isEmpty()) {
            showMessage("Please enter Complain");
            return false;
        }
        else {

            return true;
        }
    }

    private void UpdateComplaints() {
        loading(true);
        //startActivity(new Intent(getApplicationContext(), SignInActivity.class));

        HashMap hashMap = new HashMap();

        hashMap.put("address",address);
        hashMap.put("city",city);
        hashMap.put("zipCode",zipCode);
        hashMap.put("subject",subject);
        hashMap.put("complain",complain);

        reference.child(userID).child(compID).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                showMessage("Updated successfully");
                loading(false);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage("something went wrong");
                loading(false);
            }
        });

    }



    private void loading(Boolean isLoading){
        if(isLoading){
            binding.btnUpdate.setVisibility(View.INVISIBLE);
            binding.pb.setVisibility(View.VISIBLE);
        }else{
            binding.btnUpdate.setVisibility(View.VISIBLE);
            binding.pb.setVisibility(View.INVISIBLE);
        }
    }



}