package com.example.crimeandmissingreport.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crimeandmissingreport.AdminMainActivity;
import com.example.crimeandmissingreport.MainActivity;
import com.example.crimeandmissingreport.R;
import com.example.crimeandmissingreport.models.ComplaintsData;
import com.example.crimeandmissingreport.models.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class AllComplaintsAdapter extends RecyclerView.Adapter<AllComplaintsAdapter.ViewHolder> {

    private List<ComplaintsData> list;
    private Context context;

    private String status1,status2;
    private String UserID,phone, email,name,surname;



    public AllComplaintsAdapter(List<ComplaintsData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.complaint_status_items,
                parent,false);
        return new AllComplaintsAdapter.ViewHolder(view);
    }
    public void filterList(List<ComplaintsData> filterList){
        list = filterList;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ComplaintsData items = list.get(position);


        SpannableString subject = new SpannableString("Subject: " + items.getSubject());
        StyleSpan boldSubject= new StyleSpan(Typeface.BOLD);

        SpannableString complaints = new SpannableString("Complaints Details: " + items.getComplain());
        StyleSpan boldComplaints = new StyleSpan(Typeface.BOLD);

        SpannableString status = new SpannableString("Status: " + items.getStatus());
        StyleSpan boldStatus = new StyleSpan(Typeface.BOLD);

        subject.setSpan(boldSubject,0,8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        complaints.setSpan(boldComplaints,0,19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        status.setSpan(boldStatus,0,8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        holder.txtSubject1.setText(subject);
        holder.txtComplain1.setText(complaints );
        holder.txtStatus1.setText(status);

        if(items.getStatus().equals("Submitted"))
        {
            holder.txtStatus1.setTextColor(Color.RED);
        }
        else{
            holder.txtStatus1.setTextColor(Color.GREEN);
        }

        holder.btnViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                View view = LayoutInflater.from(context).inflate(R.layout.all_complaint_status_dialog, null);

                builder.setView(view).setTitle("Details");


                TextView txtSubject1 = view.findViewById(R.id.txtSubject1);
                TextView txtCity1 = view.findViewById(R.id.txtCity1);
                TextView edtZipCode1 = view.findViewById(R.id.edtZipCode1);
                TextView txtComplain1 = view.findViewById(R.id.txtComplain1);
                TextView txtName1 = view.findViewById(R.id.txtName1);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"})
                TextView txtPhone1 = view.findViewById(R.id.txtPhone1);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"})
                TextView txtEmail1 = view.findViewById(R.id.txtEmail1);
                Spinner spStatus1 = view.findViewById(R.id.spStatus1);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"})
                Button btnUpdate = view.findViewById(R.id.btnUpdate);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"})
                Button btnCancel = view.findViewById(R.id.btnCancel);


                DatabaseReference reference = FirebaseDatabase
                        .getInstance().getReference("AllComplaints");

                reference.child(items.getCompID())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ComplaintsData complaintsData = snapshot.getValue(ComplaintsData.class);
                                if(complaintsData!=null)
                                {

                                    if(complaintsData.getStatus().equals("Submitted"))
                                    {
                                        holder.txtStatus1.setTextColor(Color.RED);
                                    }
                                    else{
                                        holder.txtStatus1.setTextColor(Color.GREEN);
                                    }
                                    SpannableString subject = new SpannableString("Subject: " + complaintsData.getSubject());
                                    StyleSpan boldSubject= new StyleSpan(Typeface.BOLD);

                                    SpannableString city = new SpannableString("City: " + complaintsData.getCity());
                                    StyleSpan boldCity = new StyleSpan(Typeface.BOLD);

                                    SpannableString zipCode = new SpannableString("Zip code: " + complaintsData.getZipCode());
                                    StyleSpan boldZipCode = new StyleSpan(Typeface.BOLD);

                                    SpannableString complaints = new SpannableString("Complaints Details: " + complaintsData.getComplain());
                                    StyleSpan boldComplaints = new StyleSpan(Typeface.BOLD);

                                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("UserD");
                                    reference1.child(complaintsData.getUserID())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    UserData userData = snapshot.getValue(UserData.class);
                                                    if(userData!=null)
                                                    {
                                                       phone = userData.getPhone();
                                                       email = userData.getEmail();
                                                       name = userData.getName();
                                                       surname = userData.getSurname();

                                                       txtName1.setText(surname+" "+name);
                                                       txtPhone1.setText(phone);
                                                       txtEmail1.setText(email);


                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });


                                    subject.setSpan(boldSubject,0,8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    city.setSpan(boldCity,0,5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    zipCode.setSpan(boldZipCode,0,9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    complaints.setSpan(boldComplaints,0,19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


                                    txtSubject1.setText(subject);
                                    txtCity1.setText(city);
                                    edtZipCode1.setText(zipCode);
                                    txtComplain1.setText(complaints);

                                    String[] item1 = new String[]{complaintsData.getStatus(), "Submitted", "Seen","Processing","Complete"};
                                    spStatus1.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, item1));

                                    spStatus1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                             status1 = spStatus1.getSelectedItem().toString();



                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });



                                    btnUpdate.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {


                                            //DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("AllComplaints");



                                            HashMap hashMap = new HashMap();
                                            hashMap.put("status", status1);



                                            reference.child(complaintsData.getCompID()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                                @Override
                                                public void onSuccess(Object o) {

                                                    Toast.makeText(context, "Status Updated Successfully", Toast.LENGTH_SHORT).show();



                                                    //startActivity(new Intent(getApplicationContext(),PatientRecordActivity.class));
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context,
                                                            "Something went wrong " + e.getMessage(), Toast.LENGTH_SHORT).show();

                                                }
                                            });



                                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("ComplaintsData");

                                           HashMap hashMap1 = new HashMap();
                                           hashMap1.put("status",status1);

                                            reference2.child(complaintsData.getUserID())
                                                    .child(complaintsData.getCompID()).updateChildren(hashMap1);

                                        }
                                    });

                                    btnCancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            context.startActivity(new Intent(context, AdminMainActivity.class));
                                        }
                                    });


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });




                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });




    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtSubject1,txtComplain1,txtStatus1;
        private Button btnViewMore;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSubject1 = itemView.findViewById(R.id.txtSubject1);
            txtComplain1 = itemView.findViewById(R.id.txtComplain1);
            txtStatus1 = itemView.findViewById(R.id.txtStatus1);
            btnViewMore = itemView.findViewById(R.id.btnViewMore);

        }
    }
}
