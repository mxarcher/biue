package com.mxarcher.biue.fragments.upload;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mxarcher.biue.R;
import com.mxarcher.biue.fragments.upload.dialog.HandleFullScreenDialogFragment;
import com.mxarcher.biue.fragments.upload.dialog.UserFullScreenDialogFragment;

/**
 * @Author: MXArcher Lee
 * @Date: 2022/4/21
 */
public class HandleFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // 一定要注意fragment 和
        return inflater.inflate(R.layout.fragment_handle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fab = view.findViewById(R.id.fragment_handle_fab);
        fab.setOnClickListener(v -> showDialog());
    }

    private void showDialog() {
        DialogFragment dialog= new HandleFullScreenDialogFragment();
        dialog.show(getParentFragmentManager(),"tag");
    }
}