package com.example.crimeandmissingreport;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.crimeandmissingreport.databinding.ActivityAdminMainBinding;
import com.example.crimeandmissingreport.databinding.ActivityAdminSignInBinding;
import com.example.crimeandmissingreport.databinding.ActivitySignInBinding;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminSignInActivity extends AppCompatActivity {


    private FirebaseAuth auth;
    private ActivityAdminSignInBinding binding;
    private String email, pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminSignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        setListeners();
        checkPassword();

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        });
    }

    private void setListeners() {
        binding.txtCreateAccount.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),AdminSignUpActivity.class)));
        binding.txtForgetPassword.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(),AdminResetPassActivity.class)));

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidDetails()){
                    sigIn();
                }
            }
        });
    }
    private void checkPassword() {
        binding.chb.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                binding.inputPassword.setTransformationMethod(null);
            }else{
                binding.inputPassword.setTransformationMethod(new PasswordTransformationMethod());
            }
        });
    }
    private void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
    private void loading(Boolean isLoading){
        if(isLoading){
            binding.btnSignIn.setVisibility(View.INVISIBLE);
            binding.pb.setVisibility(View.VISIBLE);
        }else{
            binding.btnSignIn.setVisibility(View.VISIBLE);
            binding.pb.setVisibility(View.INVISIBLE);
        }
    }
    private Boolean isValidDetails(){
        email = binding.inputEmail.getText().toString().trim();
        pass = binding.inputPassword.getText().toString().trim();
        if(email.isEmpty()){
            showMessage("Please enter email address");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            showMessage("Please enter a valid email address");
            return false;
        }else if(pass.isEmpty()){
            showMessage("Please enter password");
            return false;
        }else if((pass.length() != 6 && pass.length() < 6)){
            showMessage("Password has at least 6 characters ");
            return false;
        }else{
            return true;
        }

    }
    private void sigIn(){
        loading(true);
        //startActivity(new Intent(SignInActivity.this, HomeActivity.class));
        auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(AdminSignInActivity.this, AdminMainActivity.class));
                    loading(false);
                }else{
                    showMessage("Something went wrong");
                    loading(false);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage(e.getMessage());
                loading(false);
            }
        });

    }
}
