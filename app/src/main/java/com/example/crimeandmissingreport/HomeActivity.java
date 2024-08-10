package com.example.crimeandmissingreport;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.crimeandmissingreport.adapters.ViewPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;


import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    Timer timer;
    int page_position = 0;
    //LocalStorage localStorage;
    private int dotscount;
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        timer = new Timer();
        viewPager = findViewById(R.id.viewPager);

        sliderDotspanel = findViewById(R.id.SliderDots);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);

        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        scheduleSlider();
    }

    public void scheduleSlider() {

        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {
                if (page_position == dotscount) {
                    page_position = 0;
                } else {
                    page_position = page_position + 1;
                }
                viewPager.setCurrentItem(page_position, true);
            }
        };

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 500, 4000);
    }

    @Override
    protected void onStop() {
        timer.cancel();
        super.onStop();
    }

    @Override
    protected void onPause() {
        timer.cancel();
        super.onPause();
    }

    public void onLetsClicked(View view) {

        if(view.getId()==R.id.lnNextPage) {

            if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }else{
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                finish();
            }
        }
        if(view.getId()==R.id.lnNextPage1) {
            startActivity(new Intent(getApplicationContext(), AllMissingActivity.class));
            finish();
            //Toast.makeText(this, "Search Missing people", Toast.LENGTH_SHORT).show();
        }
        if(view.getId()==R.id.lnNextPage2) {
            startActivity(new Intent(getApplicationContext(), AllCrimeActivity.class));
            finish();
            //Toast.makeText(this, "Search Crime around You", Toast.LENGTH_SHORT).show();
        }
        if(view.getId()==R.id. lnNextPage4) {
            if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
                startActivity(new Intent(getApplicationContext(), AdminMainActivity.class));
                finish();
            }else{
                startActivity(new Intent(getApplicationContext(), AdminSignInActivity.class));
                finish();
            }
//            finish()
           // Toast.makeText(this, "Search Crime around You", Toast.LENGTH_SHORT).show();
        }

        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }
}