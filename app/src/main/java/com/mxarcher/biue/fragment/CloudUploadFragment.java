package com.mxarcher.biue.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mxarcher.biue.models.CloudUploadViewModel;
import com.mxarcher.biue.R;

public class CloudUploadFragment extends Fragment {

    private CloudUploadViewModel mViewModel;

    public static CloudUploadFragment newInstance() {
        return new CloudUploadFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cloud_upload_fragment, container, false);
    }
}