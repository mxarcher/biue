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

import com.google.gson.Gson;
import com.mxarcher.biue.R;
import com.mxarcher.biue.databinding.FragmentHandlingFullScreenDialogBinding;
import com.mxarcher.biue.models.Handling;
import com.mxarcher.biue.viewmodels.ConfigViewModel;
import com.mxarcher.biue.viewmodels.HandlingViewModel;
import com.mxarcher.biue.service.web.ReqBody;

import java.util.Objects;

public class HandlingFullScreenDialogFragment extends DialogFragment {
    private static final String TAG = "HandlingFullScreenDialog";
    FragmentHandlingFullScreenDialogBinding binding;

    private ConfigViewModel configViewModel;
    private HandlingViewModel handlingViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogTheme);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHandlingFullScreenDialogBinding.inflate(inflater, container, false);
        configViewModel = new ViewModelProvider(requireActivity()).get(ConfigViewModel.class);
        handlingViewModel = new ViewModelProvider(requireActivity()).get(HandlingViewModel.class);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.fragmentHandleFullToolbar.setNavigationOnClickListener(v -> dismiss());
        if (getArguments() != null) {
            Handling handling = new Gson().fromJson(getArguments().getString("info"), Handling.class);
            binding.handlingDialogSetName.setText(handling.getName());
            binding.handlingDialogComments.setText(handling.getComments());
            binding.handlingDialogSetPath.setText(handling.getPath());
            binding.handlingDialogSetAlgorithm.setText(handling.getAlgorithm());
            binding.handlingDialogSetResults.setText(handling.getResults());
        }

        binding.handlingDialogConfirm.setOnClickListener(v -> {

            Handling handling;
            if (getArguments() != null) {
                handling = new Gson().fromJson(getArguments().getString("info"), Handling.class);
            } else {
                handling = new Handling();
            }
            handling.setName(Objects.requireNonNull(binding.handlingDialogSetName.getText()).toString());
            handling.setResults(Objects.requireNonNull(binding.handlingDialogSetResults.getText()).toString());
            handling.setPath(Objects.requireNonNull(binding.handlingDialogSetPath.getText()).toString());
            handling.setAlgorithm(Objects.requireNonNull(binding.handlingDialogSetAlgorithm.getText()).toString());
            handling.setComments(Objects.requireNonNull(binding.handlingDialogComments.getText()).toString());
            ReqBody<Handling> reqBody = new ReqBody<>();
            reqBody.setModel(handling);
            String operator_key = getString(R.string.key_operator_name);
            reqBody.setOperator(configViewModel.get(operator_key));
            if (getArguments() != null) {
                handlingViewModel.updateHandling(reqBody);
            } else {
                handlingViewModel.addHandling(reqBody);
            }
            Log.d(TAG, "onViewCreated: " + reqBody);
            dismiss();
        });


    }
}