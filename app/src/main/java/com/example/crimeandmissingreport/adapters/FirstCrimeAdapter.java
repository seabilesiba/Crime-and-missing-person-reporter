package com.example.crimeandmissingreport.adapters;

import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crimeandmissingreport.R;
import com.example.crimeandmissingreport.models.CrimeData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FirstCrimeAdapter extends RecyclerView.Adapter<FirstCrimeAdapter.ViewHolder> {

    private List<CrimeData> list;
    private Context context;

    public FirstCrimeAdapter(List<CrimeData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.first_crime,parent,false);
        return new ViewHolder(view);
    }
    public void filterList(List<CrimeData> filterList){
        list = filterList;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CrimeData items = list.get(position);


        SpannableString crime = new SpannableString("Crime: " + items.getCrimeDetails());
        StyleSpan boldCrime= new StyleSpan(Typeface.BOLD);


        SpannableString status = new SpannableString("Status: " + items.getStatus());
        StyleSpan boldStatus = new StyleSpan(Typeface.BOLD);

        crime.setSpan(boldCrime,0,8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        status.setSpan(boldStatus,0,8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        holder.txtCrime1.setText(items.getCrimeDetails());
        holder.txtStatus1.setText(items.getStatus());

        if(items.getStatus().equals("Submitted"))
        {
            holder.txtStatus1.setTextColor(Color.RED);
        }



        holder.btnViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                View view = LayoutInflater.from(context).inflate(R.layout.crime_dialog, null);

                builder.setView(view).setTitle("Details");



                TextView txtStreet1 = view.findViewById(R.id.txtStreet1);
                TextView txtCity1 = view.findViewById(R.id.txtCity1);
                TextView edtZipCode1 = view.findViewById(R.id.edtZipCode1);
                TextView txtCrimeDetails1 = view.findViewById(R.id.txtCrimeDetails1);
                TextView txtStatus1 = view.findViewById(R.id.txtStatus1);
                ImageView imgCrime = view.findViewById(R.id.imgCrime);



                DatabaseReference reference = FirebaseDatabase
                        .getInstance().getReference("AllCrime");

                reference.child(items.getCrimeID())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                CrimeData crimeData = snapshot.getValue(CrimeData.class);
                                if(crimeData!=null)
                                {

                                    SpannableString street = new SpannableString("Street: " + crimeData.getStreet());
                                    StyleSpan boldStreet= new StyleSpan(Typeface.BOLD);

                                    SpannableString city = new SpannableString("City: " + items.getCity());
                                    StyleSpan boldCity = new StyleSpan(Typeface.BOLD);

                                    SpannableString zipCode = new SpannableString("Zip code: " + items.getZipCode());
                                    StyleSpan boldZipCode = new StyleSpan(Typeface.BOLD);

                                    SpannableString crimeDetail = new SpannableString("Crime Details: " + crimeData.getCrimeDetails());
                                    StyleSpan boldCrime = new StyleSpan(Typeface.BOLD);

                                    SpannableString status = new SpannableString("Status: " + items.getStatus());
                                    StyleSpan boldStatus = new StyleSpan(Typeface.BOLD);



                                    street.setSpan(boldStreet,0,8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    city.setSpan(boldCity,0,5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    zipCode.setSpan(boldZipCode,0,9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    crimeDetail.setSpan(boldCrime,0,15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    status.setSpan(boldStatus,0,8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                    txtStreet1.setText(street);
                                    txtCity1.setText(city);
                                    edtZipCode1.setText(zipCode);
                                    txtCrimeDetails1.setText(crimeDetail);
                                    txtStatus1.setText(status);

                                    try{
                                        Picasso.get().load(items.getImage()).into(imgCrime);
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


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtCrime1,txtStatus1;
        private Button btnViewMore;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCrime1 = itemView.findViewById(R.id.txtCrime1);
            txtStatus1 = itemView.findViewById(R.id.txtStatus1);
            btnViewMore = itemView.findViewById(R.id.btnViewMore);
        }
    }
}
