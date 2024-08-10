package com.example.crimeandmissingreport.fragmants;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crimeandmissingreport.R;
import com.example.crimeandmissingreport.SignInActivity;
import com.example.crimeandmissingreport.SignUpActivity;
import com.example.crimeandmissingreport.models.ComplaintsData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ComplaintFragment extends Fragment {


    private EditText edtAddress,edtCity,edtZipCode,edtSubject,edtComplain;
    private Button btnSubmit;
    private String compID,userID,address,city,zipCode,subject,complain,dateTime,status;
    private ProgressDialog pd;

    private DatabaseReference reference;
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
        View view = inflater.inflate(R.layout.fragment_complaint, container, false);
        edtAddress = view.findViewById(R.id.edtAddress);
        edtCity = view.findViewById(R.id.edtCity);
        edtZipCode = view.findViewById(R.id.edtZipCode);
        edtSubject = view.findViewById(R.id.edtSubject);
        edtComplain = view.findViewById(R.id.edtComplain);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        pd = new ProgressDialog(getContext());
                reference = FirebaseDatabase.getInstance().getReference("ComplaintsData");
        auth = FirebaseAuth.getInstance();

        setListener();

        return view;
    }
    private void setListener() {

        btnSubmit.setOnClickListener(view -> {
            if (isValidDetails()) {
                setData();
            }


        });
    }
    private void showMessage(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
    private Boolean isValidDetails() {
        address = edtAddress.getText().toString().trim();
        city = edtCity.getText().toString().trim();
        zipCode = edtZipCode.getText().toString().trim();
        subject = edtSubject.getText().toString().trim();
        complain = edtComplain.getText().toString().trim();

        if (address.isEmpty()) {
            showMessage("Please enter your address");
            return false;
        } else if (city.isEmpty()) {
            showMessage("Please enter your city");
            return false;
        }else if (zipCode.isEmpty()) {
            showMessage("Please enter your zip code");
            return false;
        } else if (subject.isEmpty()) {
            showMessage("Please enter the subject");
            return false;
        } else if (complain.isEmpty()) {
            showMessage("Please enter your complain");
            return false;
        }else{
            return true;
        }
    }
    private void setData(){
        pd.setMessage("Inserting your data...");
        pd.show();

        compID = reference.push().getKey();
        userID = auth.getCurrentUser().getUid();

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yy");
        String date = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String time = currentTime.format(calForTime.getTime());

        dateTime = date + " - " + time;
        status = "Submitted";


        ComplaintsData complaintsData = new ComplaintsData(compID,userID,address,city,zipCode,
                subject,complain,dateTime,status);
        DatabaseReference reference1 = FirebaseDatabase
                .getInstance().getReference("AllComplaints");
        reference1.child(compID).setValue(complaintsData);

        reference.child(userID).child(compID).setValue(complaintsData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            showMessage("Complain added");
                            edtAddress.setText("");
                            edtCity.setText("");
                            edtZipCode.setText("");
                            edtSubject.setText("");
                            edtComplain.setText("");

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