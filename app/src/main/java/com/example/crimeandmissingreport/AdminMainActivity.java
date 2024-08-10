package com.example.crimeandmissingreport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.crimeandmissingreport.adapters.AdminViewPagerAdapter;
import com.example.crimeandmissingreport.adapters.MyViewPagerAdapter;
import com.example.crimeandmissingreport.databinding.ActivityAdminMainBinding;
import com.example.crimeandmissingreport.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

public class AdminMainActivity extends AppCompatActivity {

    private AdminViewPagerAdapter adminViewPagerAdapter;
    private ActivityAdminMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.imgLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),SignInActivity.class));
                Toast.makeText(AdminMainActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
            }
        });
        adminViewPagerAdapter = new AdminViewPagerAdapter(this);
        binding.ViewPager.setAdapter(adminViewPagerAdapter);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                binding.tabLayout, binding.ViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Complaints");

                        //tab.setIcon(R.drawable.baseline_home_24);
                        break;
                    case 1:
                        tab.setText("Crimes");
                        //tab.setIcon(R.drawable.baseline_notifications_24);
                        break;
                    case 2:
                        tab.setText("Missing People");
                        //tab.setIcon(R.drawable.baseline_settings_24);
                        break;

                    default:
                }
            }
        }
        );
        tabLayoutMediator.attach();

    }
}
