package com.example.crimeandmissingreport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.crimeandmissingreport.adapters.MyViewPagerAdapter;
import com.example.crimeandmissingreport.databinding.ActivityMainBinding;
import com.example.crimeandmissingreport.models.UserData;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private MyViewPagerAdapter myViewPagerAdapter;
    private ActivityMainBinding binding;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        reference = FirebaseDatabase.getInstance().getReference("UserD");
        getImageProfile();
        binding.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
            }
        });
        binding.imgLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                Toast.makeText(MainActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
            }
        });
        myViewPagerAdapter = new MyViewPagerAdapter(this);
        binding.ViewPager.setAdapter(myViewPagerAdapter);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                binding.tabLayout, binding.ViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Comp");

                        //tab.setIcon(R.drawable.baseline_home_24);
                        break;
                    case 1:
                        tab.setText("Crime");
                        //tab.setIcon(R.drawable.baseline_notifications_24);
                        break;
                    case 2:
                        tab.setText("Miss");
                        //tab.setIcon(R.drawable.baseline_settings_24);
                        break;
                    case 3:
                        tab.setText("Comp Status");
                        //tab.setIcon(R.drawable.baseline_home_24);
                        break;
                    case 4:
                        tab.setText("Crime Status");
                        //tab.setIcon(R.drawable.baseline_notifications_24);
                        break;
                    case 5:
                        tab.setText("Miss status");
                        //tab.setIcon(R.drawable.baseline_settings_24);
                        break;
                    default:
                }
            }
        }
        );
        tabLayoutMediator.attach();

    }

    private void getImageProfile(){

        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserData userData = snapshot.getValue(UserData.class);
                        if(userData!=null){
                            String image = userData.getImage();
                            try {
                                Picasso.get().load(image).into(binding.imgProfile);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else{
                            Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
