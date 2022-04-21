package com.mxarcher.biue.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mxarcher.biue.R;
import com.mxarcher.biue.models.ConfigViewModel;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private ConfigViewModel configViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    /**
     * 此函数可以用来绑定组件
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configViewModel = new ViewModelProvider(requireActivity()).get(ConfigViewModel.class);
        String username = configViewModel.getUsername();
        TextView tv = view.findViewById(R.id.textView);
        Log.d(TAG, "onViewCreated: "+ username);
        tv.setText(username);
    }

}