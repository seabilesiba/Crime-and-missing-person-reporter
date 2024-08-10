package com.example.crimeandmissingreport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.crimeandmissingreport.databinding.ActivityProfileBinding;
import com.example.crimeandmissingreport.models.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {


    private DatabaseReference mRf;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String userID;


    private String name, surname, phone, email,image,location;
    private ActivityProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mRf = FirebaseDatabase.getInstance().getReference().child("UserD");
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();


        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        binding.btnUpdate.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), UpdateProfileActivity.class)));


        displayProfile();

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
    private void displayProfile() {

        loading(true);
        mRf.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                    binding.txtNameSurename.setText(surname+ " "+ name);
                    binding.txtPhoneNum.setText(phone);
                    binding.txtEmail.setText(email);
                    binding.txtLocation.setText(location);

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

                Toast.makeText(ProfileActivity.this, "Something went Wrong "+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
