package com.mxarcher.biue.fragments.upload;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mxarcher.biue.R;
import com.mxarcher.biue.fragments.upload.dialog.CollectionFullScreenDialogFragment;
import com.mxarcher.biue.fragments.upload.dialog.HandleFullScreenDialogFragment;

public class CollectionFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fab = view.findViewById(R.id.fragment_collection_fab);
        fab.setOnClickListener(v -> showDialog());
    }

    private void showDialog() {
        DialogFragment dialog= new CollectionFullScreenDialogFragment();
        dialog.show(getParentFragmentManager(),"tag");

    }
}