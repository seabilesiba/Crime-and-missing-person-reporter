package com.example.crimeandmissingreport.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crimeandmissingreport.R;
import com.example.crimeandmissingreport.UpdateComplaintsActivity;
import com.example.crimeandmissingreport.models.ComplaintsData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ComplaintsAdapter extends RecyclerView.Adapter<ComplaintsAdapter.ViewHolder> {

    private List<ComplaintsData> list;
    private Context context;

    public ComplaintsAdapter(List<ComplaintsData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.complaint_status_items,
                parent,false);
        return new ViewHolder(view);
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

        holder.btnViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                View view = LayoutInflater.from(context).inflate(R.layout.complain_dialog, null);

                builder.setView(view).setTitle("Details");


                TextView txtSubject1 = view.findViewById(R.id.txtSubject1);
                TextView txtCity1 = view.findViewById(R.id.txtCity1);
                TextView edtZipCode1 = view.findViewById(R.id.edtZipCode1);
                TextView txtComplain1 = view.findViewById(R.id.txtComplain1);
                TextView txtStatus1 = view.findViewById(R.id.txtStatus1);



                DatabaseReference reference = FirebaseDatabase
                        .getInstance().getReference("AllComplaints");

                reference.child(items.getCompID())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ComplaintsData complaintsData = snapshot.getValue(ComplaintsData.class);
                        if(complaintsData!=null)
                        {

                            SpannableString subject = new SpannableString("Subject: " + complaintsData.getSubject());
                            StyleSpan boldSubject= new StyleSpan(Typeface.BOLD);

                            SpannableString city = new SpannableString("City: " + items.getCity());
                            StyleSpan boldCity = new StyleSpan(Typeface.BOLD);

                            SpannableString zipCode = new SpannableString("Zip code: " + items.getZipCode());
                            StyleSpan boldZipCode = new StyleSpan(Typeface.BOLD);

                            SpannableString complaints = new SpannableString("Complaints Details: " + complaintsData.getComplain());
                            StyleSpan boldComplaints = new StyleSpan(Typeface.BOLD);

                            SpannableString status = new SpannableString("Status: " + items.getStatus());
                            StyleSpan boldStatus = new StyleSpan(Typeface.BOLD);



                            subject.setSpan(boldSubject,0,8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            city.setSpan(boldCity,0,5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            zipCode.setSpan(boldZipCode,0,9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            complaints.setSpan(boldComplaints,0,19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            status.setSpan(boldStatus,0,8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                            txtSubject1.setText(subject);
                            txtCity1.setText(city);
                            edtZipCode1.setText(zipCode);
                            txtComplain1.setText(complaints);
                            txtStatus1.setText(status);
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

        holder.lnContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(context, UpdateComplaintsActivity.class);

                intent.putExtra("compID",items.getCompID());
                intent.putExtra("userID",items.getUserID());
                intent.putExtra("address",items.getAddress());
                intent.putExtra("city",items.getCity());
                intent.putExtra("zipCode",items.getZipCode());
                intent.putExtra("subject",items.getSubject());
                intent.putExtra("complain",items.getComplain());
                context.startActivity(intent);

                return true;
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
        private LinearLayout lnContainer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtSubject1 = itemView.findViewById(R.id.txtSubject1);
            txtComplain1 = itemView.findViewById(R.id.txtComplain1);
            txtStatus1 = itemView.findViewById(R.id.txtStatus1);
            btnViewMore = itemView.findViewById(R.id.btnViewMore);
            lnContainer = itemView.findViewById(R.id.lnContainer);
        }
    }
}
