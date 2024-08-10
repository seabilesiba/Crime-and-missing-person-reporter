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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.crimeandmissingreport.R;
import com.example.crimeandmissingreport.models.CrimeData;
import com.example.crimeandmissingreport.models.MissingData;
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


public class MissingPeopleFragment extends Fragment {


    private EditText edtName,edtAge,edtLastSeen,edtDetails,edtZipCode;
    private CheckBox chkMale,chkFemale;
    private ImageView imgMissingPerson;
    private Button btnSelectMissingPerson,btnSubmit;
    private String missID,userID,name,age,gender,lastSeen,details,dateTime,status,image,zipCode;
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
        View view = inflater.inflate(R.layout.fragment_missing_people, container, false);

        edtName = view.findViewById(R.id.edtName);
        edtAge = view.findViewById(R.id.edtAge);
        edtLastSeen = view.findViewById(R.id.edtLastSeen);
        edtDetails = view.findViewById(R.id.edtDetails);
        edtZipCode = view.findViewById(R.id.edtZipCode);
        chkMale = view.findViewById(R.id.chkMale);
        chkFemale = view.findViewById(R.id.chkFemale);
        imgMissingPerson = view.findViewById(R.id.imgMissingPerson);
        btnSelectMissingPerson = view.findViewById(R.id.btnSelectMissingPerson);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        pd = new ProgressDialog(getContext());
        reference = FirebaseDatabase.getInstance().getReference("MissData");
        storageReference = FirebaseStorage.getInstance().getReference("MissData");
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

        btnSelectMissingPerson.setOnClickListener(v -> {
            openGallery();

        });



    }
    private void showMessage(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,REQ);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK){
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),uri);

            }catch (Exception exception){
                exception.printStackTrace();
            }



            imgMissingPerson.setImageBitmap(bitmap);


        }

    }


    private Boolean isValidDetails() {
        name = edtName.getText().toString().trim();
        age = edtAge.getText().toString().trim();
        lastSeen = edtLastSeen.getText().toString().trim();
        details = edtDetails.getText().toString().trim();
        zipCode = edtZipCode.getText().toString().trim();

        if (bitmap == null ) {
            showMessage("Please select image");
            return false;
        } else if (name.isEmpty()) {
            showMessage("Please enter name");
            return false;
        } else if (age.isEmpty()) {
            showMessage("Please enter age");
            return false;
        }else if (lastSeen.isEmpty()) {
            showMessage("Please enter last seen place");
            return false;
        } else if (details.isEmpty()) {
            showMessage("Please enter details of missing person");
            return false;
        }else if(!chkMale.isChecked() && !chkFemale.isChecked()){
            showMessage(" Please select gender");
            return false;
        }else if(chkMale.isChecked() && chkFemale.isChecked()){
            showMessage("please select one gender");
            return false;
        } else if(zipCode.isEmpty()){
            showMessage("please enter zip code");
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

        missID = reference.push().getKey();
        userID = auth.getCurrentUser().getUid();

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yy");
        String date = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String time = currentTime.format(calForTime.getTime());

        dateTime = date + " - " + time;
        status = "Submitted";

        if(chkMale.isChecked()){
            gender = "Male";

        }
        if(chkFemale.isChecked()){
            gender = "Female";

        }


        MissingData missingData = new MissingData(missID,userID,name,age,gender,lastSeen,
                details,dateTime,status,downloadUrl,zipCode);
        DatabaseReference reference1 = FirebaseDatabase
                .getInstance().getReference("AllMissing");
        reference1.child(missID).setValue(missingData);

        reference.child(userID).child(missID).setValue(missingData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            showMessage("Complain added");
                            edtName.setText("");
                            edtAge.setText("");
                            edtLastSeen.setText("");
                            edtDetails.setText("");

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