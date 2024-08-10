package com.example.crimeandmissingreport.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.crimeandmissingreport.fragmants.AllComplaintsFragment;
import com.example.crimeandmissingreport.fragmants.AllCrimesFragment;
import com.example.crimeandmissingreport.fragmants.AllMissingsFragment;
import com.example.crimeandmissingreport.fragmants.ComplaintFragment;
import com.example.crimeandmissingreport.fragmants.ComplaintsStatusFragment;
import com.example.crimeandmissingreport.fragmants.CrimeFragment;
import com.example.crimeandmissingreport.fragmants.CrimeStatusFragment;
import com.example.crimeandmissingreport.fragmants.MissingPeopleFragment;
import com.example.crimeandmissingreport.fragmants.MissingPeopleStatusFragment;

public class AdminViewPagerAdapter extends FragmentStateAdapter {



    public AdminViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new AllComplaintsFragment();
            case 1:
                return new AllCrimesFragment();
            case 2:
                return new AllMissingsFragment();
            default:
                return new AllComplaintsFragment();

        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
