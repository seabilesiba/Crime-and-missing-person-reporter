package com.example.crimeandmissingreport.fragmants;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.crimeandmissingreport.R;
import com.example.crimeandmissingreport.SignUpActivity;
import com.example.crimeandmissingreport.models.ComplaintsData;
import com.example.crimeandmissingreport.models.CrimeData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CrimeFragment extends Fragment {

    private EditText edtStreet,edtCity,edtZipCode,edtCrime;
    private ImageView imgCrime;
    private Button btnSelectCrime,btnSubmit,btnOpenCamera,btnOpenGallery;
    private LinearLayout lnImgType;

    private String crimeID,userID,street,city,zipCode,crimeDetails,dateTime,status,image;
    private final int REQ = 1;
    private Bitmap bitmap, bitmap1;
    String downloadUrl,downloadUrl1;
    private ProgressDialog pd;
    private DatabaseReference reference;
    private StorageReference storageReference;
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
        View view = inflater.inflate(R.layout.fragment_crime, container, false);

        edtStreet = view.findViewById(R.id.edtStreet);
        edtCity = view.findViewById(R.id.edtCity);
        edtZipCode = view.findViewById(R.id.edtZipCode);
        edtCrime = view.findViewById(R.id.edtCrime);
        imgCrime = view.findViewById(R.id.imgCrime);
        btnSelectCrime = view.findViewById(R.id.btnSelectCrime);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnOpenCamera = view.findViewById(R.id.btnOpenCamera);
        btnOpenGallery = view.findViewById(R.id.btnOpenGallery);
        lnImgType = view.findViewById(R.id.lnImgType);
        pd = new ProgressDialog(getContext());

        reference = FirebaseDatabase.getInstance().getReference("CrimeData");
        storageReference = FirebaseStorage.getInstance().getReference("CrimeData");
        auth = FirebaseAuth.getInstance();

        setListener();

        return view;
    }
    private void setListener() {

        btnSubmit.setOnClickListener(view -> {
            if (isValidDetails()) {
                uploadImage();

            }
        });

        btnSelectCrime.setOnClickListener(v -> {
            lnImgType.setVisibility(View.VISIBLE);

        });

        btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
                lnImgType.setVisibility(View.GONE);
            }
        });
        btnOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
                lnImgType.setVisibility(View.GONE);
            }
        });

    }
    private void showMessage(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,REQ);
    }

    private void openCamera() {
        try {
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQ);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK){
            Uri uri = data.getData();
            try {
               bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),uri);
              // bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),uri);

            }catch (Exception exception){
                exception.printStackTrace();
            }



            imgCrime.setImageBitmap(bitmap);

        }else {
            bitmap1 = (Bitmap) data.getExtras().get("data");

            imgCrime.setImageBitmap(bitmap1);
        }

    }


    private Boolean isValidDetails() {
        street = edtStreet.getText().toString().trim();
        city = edtCity.getText().toString().trim();
        zipCode = edtZipCode.getText().toString().trim();
        crimeDetails = edtCrime.getText().toString().trim();

        if (bitmap == null && bitmap1==null) {
            showMessage("Please select image");
            return false;
        } else if (street.isEmpty()) {
            showMessage("Please enter your address");
            return false;
        } else if (city.isEmpty()) {
            showMessage("Please enter your city");
            return false;
        }else if (zipCode.isEmpty()) {
            showMessage("Please enter your zip code");
            return false;
        } else if (crimeDetails.isEmpty()) {
            showMessage("Please enter the crime Details");
            return false;
        }else{
            return true;
        }
    }
    private void uploadImage(){

        pd.setMessage("Inserting data...");
        pd.show();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50,baos);

        byte[] finalimg = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child(finalimg+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(getActivity(), task -> {
            if(task.isSuccessful()){
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(uri -> {
                            downloadUrl = String.valueOf(uri);
                            setData();
                            pd.dismiss();

                        });
                    }
                });
            }else {
                showMessage("wrong");
                pd.dismiss();
            }
        });
    }

    private void setData(){
        pd.setMessage("Inserting data...");
        pd.show();

        crimeID = reference.push().getKey();
        userID = auth.getCurrentUser().getUid();

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yy");
        String date = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String time = currentTime.format(calForTime.getTime());

        dateTime = date + " - " + time;
        status = "Submitted";

        //private String crimeID,userID,street,city,zipCode,crimeDetails,date,status,image;

        CrimeData crimeData = new CrimeData(crimeID,userID,street,city,zipCode,
                crimeDetails,dateTime,status,downloadUrl);
        DatabaseReference reference1 = FirebaseDatabase
                .getInstance().getReference("AllCrime");
        reference1.child(crimeID).setValue(crimeData);

        reference.child(userID).child(crimeID).setValue(crimeData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            showMessage("Complain added");
                            edtStreet.setText("");
                            edtCity.setText("");
                            edtZipCode.setText("");
                            edtCrime.setText("");

                            pd.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("Something went wrong: "+e.getMessage());
                        pd.dismiss();
                    }
                });



    }
}