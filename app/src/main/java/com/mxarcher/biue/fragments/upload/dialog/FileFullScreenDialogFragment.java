package com.mxarcher.biue.fragments.upload.dialog;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.mxarcher.biue.R;
import com.mxarcher.biue.adapters.CollectionSpinnerAdapter;
import com.mxarcher.biue.databinding.FragmentFileFullScreenDialogBinding;
import com.mxarcher.biue.models.Collection;
import com.mxarcher.biue.viewmodels.CollectionViewModel;
import com.mxarcher.biue.viewmodels.ConfigViewModel;
import com.mxarcher.biue.viewmodels.FileViewModel;
import com.mxarcher.biue.service.web.ReqBody;

import java.util.List;

public class FileFullScreenDialogFragment extends DialogFragment {
    private static final String TAG = "FileFullScreenDialogFragment";
    FragmentFileFullScreenDialogBinding binding;
    private CollectionViewModel collectionViewModel;
    private FileViewModel fileViewModel;
    private ConfigViewModel configViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogTheme);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFileFullScreenDialogBinding.inflate(inflater, container, false);

        configViewModel = new ViewModelProvider(requireActivity()).get(ConfigViewModel.class);
        collectionViewModel = new ViewModelProvider(requireActivity()).get(CollectionViewModel.class);
        fileViewModel = new ViewModelProvider(requireActivity()).get(FileViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() == null) {
            dismiss();
        }
        binding.fragmentFileFullToolbar.setNavigationOnClickListener(v -> dismiss());
        List<Collection> collectionList = collectionViewModel.getCollectionList();
        CollectionSpinnerAdapter adapter = new CollectionSpinnerAdapter(requireContext(), collectionList);
        binding.fileDialogSelectCollection.setAdapter(adapter);
        binding.fileDialogConfirm.setOnClickListener(v -> {
            Collection collection = (Collection) binding.fileDialogSelectCollection.getSelectedItem();
            String filename = getArguments().getString("filename");
            collection.setPath("/static/" + filename);
            ReqBody<Collection> reqBody = new ReqBody<>();
            reqBody.setModel(collection);
            String operator_key = getString(R.string.key_operator_name);
            reqBody.setOperator(configViewModel.get(operator_key));
            collectionViewModel.updateCollection(reqBody);
            Log.d(TAG, "onViewCreated: " + collection);
            dismiss();
        });

    }
}