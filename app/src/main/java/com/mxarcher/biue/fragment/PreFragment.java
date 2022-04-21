package com.mxarcher.biue.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.mxarcher.biue.R;
import com.mxarcher.biue.models.ConfigViewModel;

public class PreFragment extends Fragment {
    private ConfigViewModel viewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pre, container, false);
    }

    // 建议在此函数中进行绑定操作
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ConfigViewModel.class);

        // 初始化配置文件
        SharedPreferences shp = requireActivity().getSharedPreferences(getString(R.string.config_file_name), Context.MODE_PRIVATE);

        // 判断配置文件中是否存在已经保存的操作者信息
        String key = getString(R.string.username_key);
        if (shp.contains(key)) {
            String username = shp.getString(key, getString(R.string.username_default));
            enter(username);
            return;
        }
        view.findViewById(R.id.button).setOnClickListener(view1 -> {
                    EditText editText = view.findViewById(R.id.editTextTextPersonName);
                    String username = editText.getText().toString();
                    enter(username);
                }
        );


    }

    private void enter(String username) {
        // 如果输入框中输出的数据为空，则不进行后续操作
        if(TextUtils.isEmpty(username)) {
            Toast.makeText(getActivity(),"请输入名字",Toast.LENGTH_SHORT).show();
            return;
        }
        viewModel.setUsername(username);
        NavHostFragment.findNavController(this).popBackStack();
        NavHostFragment.findNavController(this).navigate(R.id.homeFragment);
    }
}