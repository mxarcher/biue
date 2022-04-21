package com.mxarcher.biue.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mxarcher.biue.models.BlueToothViewModel;
import com.mxarcher.biue.R;

public class BlueToothFragment extends Fragment {

    private BlueToothViewModel mViewModel;

    public static BlueToothFragment newInstance() {
        return new BlueToothFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.blue_tooth_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BlueToothViewModel.class);
        // TODO: Use the ViewModel
    }

}