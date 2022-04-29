package com.mxarcher.biue.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.mxarcher.biue.R;
import com.mxarcher.biue.adapters.FragmentAdapter;
import com.mxarcher.biue.fragments.upload.CollectionFragment;
import com.mxarcher.biue.fragments.upload.FileFragment;
import com.mxarcher.biue.fragments.upload.HandlingFragment;
import com.mxarcher.biue.fragments.upload.UserFragment;

public class CloudUploadFragment extends Fragment {
    private static final String TAG = "CloudUploadFragment";
    ViewPager2 viewPager2;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.cloud_upload_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager2 = view.findViewById(R.id.fragment_upload_viewpager);
        //参考: https://stackoverflow.com/questions/60129178/viewpager2-tabs-problem-with-viewmodel-state
        FragmentAdapter adapter = new FragmentAdapter(getChildFragmentManager(), getLifecycle());
        adapter.addFragment(new UserFragment());
        adapter.addFragment(new CollectionFragment());
        adapter.addFragment(new HandlingFragment());
        adapter.addFragment(new FileFragment());
        viewPager2.setAdapter(adapter);
        TabLayout tabLayout = view.findViewById(R.id.fragment_upload_tablayout);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("被采集者信息");
                    break;
                case 1:
                    tab.setText("采集");
                    break;
                case 2:
                    tab.setText("数据处理");
                    break;
                default:
                    tab.setText("文件上传");
                    break;
            }
        }).attach();

//        viewModel = new ViewModelProvider(requireActivity()).get(ConfigViewModel.class);
//        测试
//        if(viewModel.isEmpty()){
//             如果viewmodel里没有配置shp,则表明没有初始化，进行初始化
//            viewModel.set(getPreferenceManager().getSharedPreferences());
//        }
    }
}

