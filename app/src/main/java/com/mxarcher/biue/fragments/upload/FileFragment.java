package com.mxarcher.biue.fragments.upload;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mxarcher.biue.R;
import com.mxarcher.biue.models.ConfigViewModel;

public class FileFragment extends Fragment {
    private static final String TAG = "FileFragment";
    ConfigViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onViewCreated: enter");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_file, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: enter");

        viewModel = new ViewModelProvider(requireActivity()).get(ConfigViewModel.class);
        if (viewModel.isEmpty()) {
            // 如果viewModel里没有配置shp,则表明没有初始化，进行初始化
            String config_file_name = getResources().getString(R.string.config_file_name);
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(config_file_name, Context.MODE_PRIVATE);
            viewModel.set(sharedPreferences);
        }
//        ArrayList<String> keys = new ArrayList<>();
        String key1 = getResources().getString(R.string.key_operator_name);
        TextView tv = view.findViewById(R.id.fragment_file_tv);
        viewModel.getConfigLiveData().observe(getViewLifecycleOwner(), stringMap -> {
            Log.d(TAG, "onViewCreated: " + stringMap.toString());
            tv.setText(String.format("%s: %s", key1, viewModel.get(key1)));
        });
    }
}