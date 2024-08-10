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
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.crimeandmissingreport.databinding.ActivityUpdateCrimes2Binding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class UpdateCrimesActivity2 extends AppCompatActivity {

    private Bitmap bitmap;
    private  final int REQ = 1;
    String downloadUrl;
    private ActivityUpdateCrimes2Binding binding;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private String crimeID,userID,street,city,zipCode,crimeDetails,date,status,image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateCrimes2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        reference = FirebaseDatabase.getInstance().getReference("CrimeData");
        storageReference = FirebaseStorage.getInstance().getReference("CrimeData");

        crimeID = getIntent().getStringExtra("crimeID");
        userID = getIntent().getStringExtra("userID");
        street = getIntent().getStringExtra("street");
        city = getIntent().getStringExtra("city");
        zipCode = getIntent().getStringExtra("zipCode");
        crimeDetails = getIntent().getStringExtra("crimeDetails");
        image = getIntent().getStringExtra("image");

        binding.inputStreet.setText(street);
        binding.inputCity.setText(city);
        binding.inputZipCode.setText(zipCode);
        binding.inputDetails.setText(crimeDetails);

        try{
            Picasso.get().load(image).into(binding.imgProfile);
        }catch (Exception e){
            e.printStackTrace();
        }



        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidDetails()){
                    uploadImage();
                }
            }
        });
        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteComplaints();
            }


        });
        binding.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(),UpdateActivity.class));
                openGallery();
            }
        });
    }
    private void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK){
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            }catch (Exception exception){
                exception.printStackTrace();
            }

            binding.imgProfile.setImageBitmap(bitmap);
        }
    }
    private void deleteComplaints() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateCrimesActivity2.this);
        builder.setMessage("Do you really want to delete this Details?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                reference.child(userID).child(crimeID).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(UpdateCrimesActivity2.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(UpdateCrimesActivity2.this,MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(UpdateCrimesActivity2.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(UpdateCrimesActivity2.this, "Something went wrong "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
        street = binding.inputStreet.getText().toString().trim();
        city = binding.inputCity.getText().toString().trim();
        zipCode = binding.inputZipCode.getText().toString().trim();
        crimeDetails = binding.inputDetails.getText().toString().trim();




        if (street.isEmpty()) {
            showMessage("Please enter street address");
            return false;
        } else if (city.isEmpty()) {
            showMessage("Please enter city");
            return false;
        } else if (zipCode.isEmpty()) {
            showMessage("Please enter zip code");
            return false;
        } else if (crimeDetails.isEmpty()) {
            showMessage("Please enter your Crime details");
            return false;
        }else  if (bitmap == null) {
            UpdateCrime(image);
            return false;
        }
        else {
            uploadImage();
            return true;
        }
    }
    private void uploadImage(){

        loading(true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50,baos);
        byte[] finalimg = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child(finalimg+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(UpdateCrimesActivity2.this, task -> {
            if(task.isSuccessful()){
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(uri -> {
                            downloadUrl = String.valueOf(uri);
                            UpdateCrime(downloadUrl);
                            loading(false);

                        });
                    }
                });
            }else {
                showMessage("wrong");
                loading(false);
            }
        });
    }
    private void UpdateCrime(String s) {
        loading(true);
        //startActivity(new Intent(getApplicationContext(), SignInActivity.class));

        HashMap hashMap = new HashMap();

        hashMap.put("street",street);
        hashMap.put("city",city);
        hashMap.put("zipCode",zipCode);
        hashMap.put("crimeDetails",crimeDetails);
        hashMap.put("image",s);

        reference.child(userID).child(crimeID).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
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