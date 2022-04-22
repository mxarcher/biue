package com.mxarcher.biue.fragments.upload;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mxarcher.biue.R;
import com.mxarcher.biue.fragments.upload.dialog.UserFullScreenDialogFragment;

public class UserFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fab = view.findViewById(R.id.fragment_user_fab);
        fab.setOnClickListener(v -> showDialog());
    }

    public void showDialog(){
        // TODO: 待更新
//        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//
//        UserFullScreenDialogFragment fullScreenDialogFragment = UserFullScreenDialogFragment.newInstance();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        transaction.add(R.id.fragmentContainerView,fullScreenDialogFragment)
//                .addToBackStack(null).commit();
        DialogFragment dialog= UserFullScreenDialogFragment.newInstance();
        dialog.show(getParentFragmentManager(),"tag");

    }
}