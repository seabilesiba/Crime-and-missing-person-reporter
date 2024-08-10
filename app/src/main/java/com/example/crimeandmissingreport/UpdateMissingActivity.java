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
import com.example.crimeandmissingreport.databinding.ActivityUpdateMissingBinding;
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

public class UpdateMissingActivity extends AppCompatActivity {
    private Bitmap bitmap;
    private  final int REQ = 1;
    String downloadUrl;

    private DatabaseReference reference;
    private StorageReference storageReference;

    private String missID,userID,name,age,gender,lastSeen,details,dateTime,status,image,zipCode;
    private ActivityUpdateMissingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateMissingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        reference = FirebaseDatabase.getInstance().getReference("MissData");
        storageReference = FirebaseStorage.getInstance().getReference("MissData");

        missID = getIntent().getStringExtra("missID");
        userID = getIntent().getStringExtra("userID");
        name = getIntent().getStringExtra("name");
        age = getIntent().getStringExtra("age");
        zipCode = getIntent().getStringExtra("zipCode");
        details = getIntent().getStringExtra("details");
        lastSeen = getIntent().getStringExtra("lastSeen");
        gender = getIntent().getStringExtra("gender");
        image = getIntent().getStringExtra("image");

        binding.inputName.setText(name);
        binding.inputAge.setText(age);
        binding.inputZipCode.setText(zipCode);
        binding.inputDetails.setText(details);
        binding.inputGender.setText(gender);
        binding.inputLastSeen.setText(lastSeen);

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
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateMissingActivity.this);
        builder.setMessage("Do you really want to delete this Details?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                reference.child(userID).child(missID).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(UpdateMissingActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(UpdateMissingActivity.this,MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(UpdateMissingActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(UpdateMissingActivity.this, "Something went wrong "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
        name = binding.inputName.getText().toString().trim();
        age = binding.inputAge.getText().toString().trim();
        zipCode = binding.inputZipCode.getText().toString().trim();
        details = binding.inputDetails.getText().toString().trim();
        lastSeen = binding.inputLastSeen.getText().toString().trim();
        gender = binding.inputGender.getText().toString().trim();




        if (name.isEmpty()) {
            showMessage("Please enter name");
            return false;
        } else if (age.isEmpty()) {
            showMessage("Please enter age");
            return false;
        } else if (zipCode.isEmpty()) {
            showMessage("Please enter zip code");
            return false;
        } else if (details.isEmpty()) {
            showMessage("Please enter your missing details");
            return false;
        } else if (lastSeen.isEmpty()) {
            showMessage("Please enter your last seen details");
            return false;
        } else if (gender.isEmpty()) {
            showMessage("Please enter your gender");
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
        uploadTask.addOnCompleteListener(UpdateMissingActivity.this, task -> {
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

        hashMap.put("name",name);
        hashMap.put("age",age);
        hashMap.put("zipCode",zipCode);
        hashMap.put("details",details);
        hashMap.put("lastSeen",lastSeen);
        hashMap.put("gender",gender);
        hashMap.put("image",s);

        reference.child(userID).child(missID).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
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