package com.example.crimeandmissingreport;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.crimeandmissingreport.databinding.ActivityAdminSignUpBinding;
import com.example.crimeandmissingreport.databinding.ActivitySignUpBinding;
import com.example.crimeandmissingreport.models.AdminData;
import com.example.crimeandmissingreport.models.UserData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class AdminSignUpActivity extends AppCompatActivity {

    private ActivityAdminSignUpBinding binding;

    private FirebaseAuth auth;
    private FirebaseUser users;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private String userID;
    private String image, surname, name,location, email, phone, pass;
    private final int REQ = 1;
    private Bitmap bitmap;
    String downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminSignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        users = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("AdminD");
        storageReference = FirebaseStorage.getInstance().getReference("AdminD");


        setListener();
        checkPassword();
    }

    private void setListener() {
        binding.txtHaveAccount.setOnClickListener(view -> startActivity(new Intent(AdminSignUpActivity.this, AdminSignInActivity.class)));
        binding.btnSignUp.setOnClickListener(view -> {
            if (isValidDetails()) {
                uploadImage();
            }


        });

        binding.imgProfile.setOnClickListener(view -> {
            openGallery();

        });

    }


    private void checkPassword() {
        binding.chb.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                binding.inputPassword.setTransformationMethod(null);
            }else{
                binding.inputPassword.setTransformationMethod(new PasswordTransformationMethod());
            }
        });
    }
    private void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void loading(Boolean isLoading){
        if(isLoading){
            binding.btnSignUp.setVisibility(View.INVISIBLE);
            binding.pb.setVisibility(View.VISIBLE);
        }else{
            binding.btnSignUp.setVisibility(View.VISIBLE);
            binding.pb.setVisibility(View.INVISIBLE);
        }
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
            binding.textAddImage.setVisibility(View.GONE);
            binding.imgProfile.setImageBitmap(bitmap);
        }
    }

    /* private String encodeImage(Bitmap bitmap){
            int previewWidth = 150;
            int preViewHeight = bitmap.getHeight() * previewWidth/ bitmap.getWidth();
            Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth,preViewHeight, false);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(bytes, Base64.DEFAULT);

        }
        private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == RESULT_OK){
                        if(result.getData() != null){
                            Uri imageUri = result.getData().getData();
                            try {
                                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                binding.imgProfile.setImageBitmap(bitmap);
                                binding.textAddImage.setVisibility(View.GONE);
                                image = encodeImage(bitmap);
                            }catch(FileNotFoundException e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );*/
    private Boolean isValidDetails() {
        surname = binding.inputSurname.getText().toString().trim();
        name = binding.inputName.getText().toString().trim();
        location = binding.inputLocation.getText().toString().trim();
        email = binding.inputEmail.getText().toString().trim();
        phone = binding.inputPhone.getText().toString().trim();
        pass = binding.inputPassword.getText().toString().trim();

        if (bitmap == null) {
            showMessage("Please select image");
            return false;
        }else if (surname.isEmpty()) {
            showMessage("Please enter your surname");
            return false;
        } else if (name.isEmpty()) {
            showMessage("Please enter your name");
            return false;
        }else if (location.isEmpty()) {
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
        }else if (pass.isEmpty()) {
            showMessage("Please enter password");
            return false;
        } else if (pass.length() != 6 && pass.length() < 6) {
            showMessage("Password has at least 6 characters");
            return false;
        }else {
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
        uploadTask.addOnCompleteListener(AdminSignUpActivity.this, task -> {
            if(task.isSuccessful()){
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(uri -> {
                            downloadUrl = String.valueOf(uri);
                            signUp();
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
    private void signUp(){
        loading(true);
        //startActivity(new Intent(getApplicationContext(), SignInActivity.class));



        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                String adminID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                AdminData adminData = new AdminData(downloadUrl, surname,location,name, email, phone, pass);

                reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(adminData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(), AdminSignInActivity.class));
                            loading(false);
                        }else {
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

        });
    }
}