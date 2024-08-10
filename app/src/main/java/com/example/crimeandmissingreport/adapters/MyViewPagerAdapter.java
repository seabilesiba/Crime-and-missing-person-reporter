package com.example.crimeandmissingreport.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.crimeandmissingreport.fragmants.ComplaintFragment;
import com.example.crimeandmissingreport.fragmants.ComplaintsStatusFragment;
import com.example.crimeandmissingreport.fragmants.CrimeFragment;
import com.example.crimeandmissingreport.fragmants.CrimeStatusFragment;
import com.example.crimeandmissingreport.fragmants.MissingPeopleFragment;
import com.example.crimeandmissingreport.fragmants.MissingPeopleStatusFragment;


public class MyViewPagerAdapter extends FragmentStateAdapter {

    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ComplaintFragment();
            case 1:
                return new CrimeFragment();
            case 2:
                return new MissingPeopleFragment();
            case 3:
                return new ComplaintsStatusFragment();
            case 4:
                return new CrimeStatusFragment();
            case 5:
                return new MissingPeopleStatusFragment();
            default:
                return new ComplaintFragment();

        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
