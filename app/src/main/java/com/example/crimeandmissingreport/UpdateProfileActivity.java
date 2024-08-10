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
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.crimeandmissingreport.databinding.ActivityUpdateProfileBinding;
import com.example.crimeandmissingreport.models.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class UpdateProfileActivity extends AppCompatActivity {
    private DatabaseReference mRf;

    private StorageReference storageReference;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String userID;
    private String name, surname, phone, email,image,location;
    private Bitmap bitmap;
    private  final int REQ = 1;
    String downloadUrl;
    private ActivityUpdateProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mRf = FirebaseDatabase.getInstance().getReference().child("UserD");
        storageReference = FirebaseStorage.getInstance().getReference("UserD");
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();


        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
                builder.setMessage("Do you really want to delete your profile?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        mRf.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            Toast.makeText(UpdateProfileActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(UpdateProfileActivity.this,SignInActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                        }else{
                                            Toast.makeText(UpdateProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(UpdateProfileActivity.this, "Something went wrong "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
        });

        updateProfile();


        binding.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(),UpdateActivity.class));
                openGallery();
            }
        });

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isValidDetails()){
                    uploadImage();
                }
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

    private Boolean isValidDetails() {

        surname = binding.inputSurname.getText().toString().trim();
        name = binding.inputName.getText().toString().trim();
        location = binding.inputLocation.getText().toString().trim();
        email = binding.inputEmail.getText().toString().trim();
        phone = binding.inputPhone.getText().toString().trim();



        if (surname.isEmpty()) {
            showMessage("Please enter your surname");
            return false;
        } else if (name.isEmpty()) {
            showMessage("Please enter your name");
            return false;
        } else if (location.isEmpty()) {
            showMessage("Please enter your Location");
            return false;
        } else if (phone.isEmpty()) {
            showMessage("Please enter your cellphone number");
            return false;
        } else if (!Patterns.PHONE.matcher(phone).matches()) {
            showMessage("Please enter a valid cellphone number");
            return false;
        } else if (phone.length() != 10 && phone.length() < 10) {
            showMessage("Cellphone number have 10 digits");
            return false;
        }else if (email.isEmpty()) {
            showMessage("Please enter email address");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showMessage("Please enter a valid email address");
            return false;
        }else  if (bitmap == null) {
            UpdateUserProfile(image);
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
        uploadTask.addOnCompleteListener(UpdateProfileActivity.this, task -> {
            if(task.isSuccessful()){
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(uri -> {
                            downloadUrl = String.valueOf(uri);
                            UpdateUserProfile(downloadUrl);
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
    private void UpdateUserProfile(String s) {
        loading(true);
        //startActivity(new Intent(getApplicationContext(), SignInActivity.class));

        HashMap hashMap = new HashMap();

        hashMap.put("email",email);
        hashMap.put("name",name);
        hashMap.put("surname",surname);
        hashMap.put("phone",phone);
        hashMap.put("image",s);
        hashMap.put("location",location);

        mRf.child(userID).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
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
    private void updateProfile() {

        loading(true);
        mRf.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData userProfile = snapshot.getValue(UserData.class) ;
                if(userProfile!=null){
                    surname = userProfile.getSurname();
                    name = userProfile.getName();
                    phone = userProfile.getPhone();
                    email = userProfile.getEmail();
                    image = userProfile.getImage();
                    location = userProfile.getLocation();

                    // binding.imgProfile.setImageBitmap(image);
                    binding.inputSurname.setText(surname);
                    binding.inputName.setText(name);
                    binding.inputPhone.setText(phone);
                    binding.inputEmail.setText(email);
                    binding.inputLocation.setText(location);

                    try {
                        Picasso.get().load(image).into(binding.imgProfile);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    loading(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}