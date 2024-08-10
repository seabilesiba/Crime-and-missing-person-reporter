package com.example.crimeandmissingreport.adapters;




import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crimeandmissingreport.R;
import com.example.crimeandmissingreport.UpdateCrimesActivity2;
import com.example.crimeandmissingreport.UpdateMissingActivity;
import com.example.crimeandmissingreport.models.ComplaintsData;
import com.example.crimeandmissingreport.models.CrimeData;
import com.example.crimeandmissingreport.models.MissingData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MissingAdapter extends RecyclerView.Adapter<MissingAdapter.ViewHolder> {

    private List<MissingData> list;
    private Context context;

    public MissingAdapter(List<MissingData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.missing_status_items,
                parent,false);
        return new ViewHolder(view);
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

                View view = LayoutInflater.from(context).inflate(R.layout.missing_dialog, null);

                builder.setView(view).setTitle("Details");



                TextView txtName1 = view.findViewById(R.id.txtName1);
                TextView txtAge1 = view.findViewById(R.id.txtAge1);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"})
                TextView txtGender1 = view.findViewById(R.id.txtGender1);
                TextView txtLastSeen1 = view.findViewById(R.id.txtLastSeen1);
                TextView txtDetails1 = view.findViewById(R.id.txtDetails1);
                TextView txtStatus1 = view.findViewById(R.id.txtStatus1);
                ImageView imgMissing = view.findViewById(R.id.imgMissing);



                DatabaseReference reference = FirebaseDatabase
                        .getInstance().getReference("AllMissing");

                reference.child(items.getMissID())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                MissingData missingData = snapshot.getValue(MissingData.class);
                                if(missingData!=null)
                                {

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

                                    SpannableString status = new SpannableString("Status: " + items.getStatus());
                                    StyleSpan boldStatus = new StyleSpan(Typeface.BOLD);



                                    name.setSpan(boldName,0,6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    age.setSpan(boldAge,0,5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    gender.setSpan(boldGender,0,7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    lastSeen.setSpan(boldLastSeen,0,11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    details.setSpan(boldDetails,0,18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    status.setSpan(boldStatus,0,8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                    txtName1.setText(name);
                                    txtAge1.setText(age);
                                    txtGender1.setText(gender);
                                    txtLastSeen1.setText(lastSeen);
                                    txtDetails1.setText(details);
                                    txtStatus1.setText(status);

                                    try{
                                        Picasso.get().load(items.getImage()).into(imgMissing);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
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
                Intent intent = new Intent(context, UpdateMissingActivity.class);


                intent.putExtra("missID",items.getMissID());
                intent.putExtra("userID",items.getUserID());
                intent.putExtra("name",items.getName());
                intent.putExtra("age",items.getAge());
                intent.putExtra("gender",items.getGender());
                intent.putExtra("details",items.getDetails());
                intent.putExtra("image",items.getImage());
                intent.putExtra("zipCode",items.getZipCode());
                intent.putExtra("lastSeen",items.getLastSeen());
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
        private TextView txtName1,txtStatus1;
        private ImageView imgMissing;
        private Button btnViewMore;
        private LinearLayout lnContainer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName1 = itemView.findViewById(R.id.txtName1);
            txtStatus1 = itemView.findViewById(R.id.txtStatus1);
            imgMissing = itemView.findViewById(R.id.imgMissing);
            btnViewMore = itemView.findViewById(R.id.btnViewMore);
            lnContainer = itemView.findViewById(R.id.lnContainer);
        }
    }
}
