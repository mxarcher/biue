package com.mxarcher.biue.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mxarcher.biue.R;
import com.mxarcher.biue.viewmodels.WavesViewModel;

public class WavesFragment extends Fragment {

    private WavesViewModel mViewModel;

    public static WavesFragment newInstance() {
        return new WavesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.waves_fragment, container, false);
    }

}