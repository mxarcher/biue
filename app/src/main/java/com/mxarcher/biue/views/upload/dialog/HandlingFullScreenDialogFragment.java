package com.mxarcher.biue.views.upload.dialog;

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
import com.mxarcher.biue.databinding.FragmentHandlingFullScreenDialogBinding;
import com.mxarcher.biue.models.Handling;
import com.mxarcher.biue.viewmodels.ConfigViewModel;
import com.mxarcher.biue.viewmodels.HandlingViewModel;
import com.mxarcher.biue.web.ReqBody;

import java.util.Objects;

public class HandlingFullScreenDialogFragment extends DialogFragment {
    private static final String TAG = "HandlingFullScreenDialog";
    FragmentHandlingFullScreenDialogBinding binding;

    private ConfigViewModel configViewModel;
    private HandlingViewModel handlingViewModel;

    public static HandlingFullScreenDialogFragment newInstance() {
        return new HandlingFullScreenDialogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogTheme);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
        binding.handlingDialogConfirm.setOnClickListener(v -> {
            Handling handling = new Handling();
            handling.setName(Objects.requireNonNull(binding.handlingDialogSetName.getText()).toString());
            handling.setResults(Objects.requireNonNull(binding.handlingDialogSetResults.getText()).toString());
            handling.setPath(Objects.requireNonNull(binding.handlingDialogSetPath.getText()).toString());
            handling.setAlgorithm(Objects.requireNonNull(binding.handlingDialogSetAlgorithm.getText()).toString());
            handling.setComments(Objects.requireNonNull(binding.handlingDialogComments.getText()).toString());
            ReqBody<Handling> reqBody = new ReqBody<>();
            reqBody.setModel(handling);
            String operator_key = getString(R.string.key_operator_name);
            reqBody.setOperator(configViewModel.get(operator_key));
            handlingViewModel.addHandling(reqBody);
            Log.d(TAG, "onViewCreated: " + reqBody);
            dismiss();
        });


        // TODO: 增加添加Handling的部分


    }
}