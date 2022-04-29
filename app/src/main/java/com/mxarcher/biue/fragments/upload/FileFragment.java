package com.mxarcher.biue.fragments.upload;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mxarcher.biue.adapters.FileListAdapter;
import com.mxarcher.biue.databinding.FragmentFileBinding;
import com.mxarcher.biue.viewmodels.ConfigViewModel;
import com.mxarcher.biue.viewmodels.FileViewModel;
import com.mxarcher.biue.fragments.upload.dialog.FileFullScreenDialogFragment;

// TODO: 实现下拉刷新
public class FileFragment extends Fragment {
    private static final String TAG = "FileFragment";
    ConfigViewModel configViewModel;
    FileListAdapter adapter;
    FileViewModel fileViewModel;
    FragmentFileBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFileBinding.inflate(inflater, container, false);

        configViewModel = new ViewModelProvider(requireActivity()).get(ConfigViewModel.class);
        fileViewModel = new ViewModelProvider(requireActivity()).get(FileViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (adapter == null) {
            adapter = new FileListAdapter(new FileListAdapter.Callback() {
                @Override
                public void onUpload(String filename) {
                    Bundle bundle = new Bundle();
                    bundle.putString("filename", filename);
                    showDialog(bundle);
                }

                @Override
                public void onDelete(String filename) {
                    if (fileViewModel.deleteFile(filename)) {
                        Log.d(TAG, "onDelete: 删除成功");
                    }
                }
            });
        }


        LinearLayoutManager llm = new LinearLayoutManager(requireContext());
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        binding.fileList.setLayoutManager(llm);
        binding.fileList.setAdapter(adapter);
        fileViewModel.getLiveData().observe(getViewLifecycleOwner(), strings -> {
            adapter.setFileList(strings);
            Log.d(TAG, "onViewCreated: obverse" + strings);
        });

    }

    private void showDialog(Bundle bundle) {
        if(bundle == null){
            return;
        }
        DialogFragment dialog = new FileFullScreenDialogFragment();
        dialog.setArguments(bundle);
        dialog.show(getParentFragmentManager(), "file");
    }
}