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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crimeandmissingreport.AdminMainActivity;
import com.example.crimeandmissingreport.R;
import com.example.crimeandmissingreport.models.MissingData;
import com.example.crimeandmissingreport.models.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class AllMissingAdapter extends RecyclerView.Adapter<AllMissingAdapter.ViewHolder> {
    private List<MissingData> list;
    private Context context;

    private String status1,status2;
    private String UserID,phone, email,name1,surname;

    public AllMissingAdapter(List<MissingData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AllMissingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.missing_status_items,
                parent,false);
        return new AllMissingAdapter.ViewHolder(view);
    }



    public void filterList(List<MissingData> filterList){
        list = filterList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MissingData items = list.get(position);


        SpannableString name = new SpannableString("Name: " + items.getName());
        StyleSpan boldName= new StyleSpan(Typeface.BOLD);


        SpannableString status = new SpannableString("Status: " + items.getStatus());
        StyleSpan boldStatus = new StyleSpan(Typeface.BOLD);

        name.setSpan(boldName,0,6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        status.setSpan(boldStatus,0,8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        holder.txtName1.setText(name);
        holder.txtStatus1.setText(status);

        try {
            Picasso.get().load(items.getImage()).into(holder.imgMissing);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(items.getStatus().equals("Submitted"))
        {
            holder.txtStatus1.setTextColor(Color.RED);
        }


        holder.btnViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                View view = LayoutInflater.from(context).inflate(R.layout.all_missing_dialog, null);

                builder.setView(view).setTitle("Details");



                TextView txtName1 = view.findViewById(R.id.txtName1);
                TextView txtAge1 = view.findViewById(R.id.txtAge1);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"})
                TextView txtGender1 = view.findViewById(R.id.txtGender1);
                TextView txtLastSeen1 = view.findViewById(R.id.txtLastSeen1);
                TextView txtDetails1 = view.findViewById(R.id.txtDetails1);
                TextView txtStatus1 = view.findViewById(R.id.txtStatus1);
                ImageView imgMissing = view.findViewById(R.id.imgMissing);

                @SuppressLint({"MissingInflatedId", "LocalSuppress"})
                TextView txtName2 = view.findViewById(R.id.txtName2);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"})
                TextView txtPhone1 = view.findViewById(R.id.txtPhone1);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"})
                TextView txtEmail1 = view.findViewById(R.id.txtEmail1);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"})
                Spinner spStatus1 = view.findViewById(R.id.spStatus1);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"})
                Button btnUpdate = view.findViewById(R.id.btnUpdate);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"})
                Button btnCancel = view.findViewById(R.id.btnCancel);



                DatabaseReference reference = FirebaseDatabase
                        .getInstance().getReference("AllMissing");

                reference.child(items.getMissID())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                MissingData missingData = snapshot.getValue(MissingData.class);
                                if(missingData!=null)
                                {

                                    if(missingData.getStatus().equals("Submitted"))
                                    {
                                        holder.txtStatus1.setTextColor(Color.RED);
                                    }
                                    else{
                                        holder.txtStatus1.setTextColor(Color.GREEN);
                                    }
                                    SpannableString name = new SpannableString("Name: " + missingData.getName());
                                    StyleSpan boldName= new StyleSpan(Typeface.BOLD);

                                    SpannableString age = new SpannableString("Age: " + missingData.getAge());
                                    StyleSpan boldAge = new StyleSpan(Typeface.BOLD);

                                    SpannableString gender = new SpannableString("gender: " + missingData.getGender());
                                    StyleSpan boldGender = new StyleSpan(Typeface.BOLD);

                                    SpannableString lastSeen = new SpannableString("Last seen: " + missingData.getLastSeen());
                                    StyleSpan boldLastSeen = new StyleSpan(Typeface.BOLD);

                                    SpannableString details = new SpannableString("person's Details: " + missingData.getDetails());
                                    StyleSpan boldDetails = new StyleSpan(Typeface.BOLD);

//                                    SpannableString status = new SpannableString("Status: " + items.getStatus());
//                                    StyleSpan boldStatus = new StyleSpan(Typeface.BOLD);


                                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("UserD");
                                    reference1.child(missingData.getUserID())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    UserData userData = snapshot.getValue(UserData.class);
                                                    if(userData!=null)
                                                    {
                                                        phone = userData.getPhone();
                                                        email = userData.getEmail();
                                                        name1 = userData.getName();
                                                        surname = userData.getSurname();

                                                        txtName2.setText(surname+" "+name1);
                                                        txtPhone1.setText(phone);
                                                        txtEmail1.setText(email);


                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });



                                    name.setSpan(boldName,0,6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    age.setSpan(boldAge,0,5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    gender.setSpan(boldGender,0,7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    lastSeen.setSpan(boldLastSeen,0,11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    details.setSpan(boldDetails,0,18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    //status.setSpan(boldStatus,0,8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                    txtName1.setText(name);
                                    txtAge1.setText(age);
                                    txtGender1.setText(gender);
                                    txtLastSeen1.setText(lastSeen);
                                    txtDetails1.setText(details);


                                    try{
                                        Picasso.get().load(items.getImage()).into(imgMissing);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }


                                    String[] item1 = new String[]{missingData.getStatus(), "Submitted", "Seen","Processing","Complete"};
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



                                            reference.child(missingData.getMissID()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
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



                                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("MissData");

                                            HashMap hashMap1 = new HashMap();
                                            hashMap1.put("status",status1);

                                            reference2.child(missingData.getUserID())
                                                    .child(missingData.getMissID()).updateChildren(hashMap1);

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
        private TextView txtName1,txtStatus1;
        private ImageView imgMissing;
        private Button btnViewMore;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName1 = itemView.findViewById(R.id.txtName1);
            txtStatus1 = itemView.findViewById(R.id.txtStatus1);
            imgMissing = itemView.findViewById(R.id.imgMissing);
            btnViewMore = itemView.findViewById(R.id.btnViewMore);
        }
    }
}
