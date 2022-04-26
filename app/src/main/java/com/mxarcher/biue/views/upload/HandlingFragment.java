package com.mxarcher.biue.views.upload;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mxarcher.biue.R;
import com.mxarcher.biue.adapters.HandlingListAdapter;
import com.mxarcher.biue.databinding.FragmentHandlingBinding;
import com.mxarcher.biue.models.AdapterCallback;
import com.mxarcher.biue.models.Handling;
import com.mxarcher.biue.viewmodels.ConfigViewModel;
import com.mxarcher.biue.viewmodels.HandlingViewModel;
import com.mxarcher.biue.views.upload.dialog.HandlingFullScreenDialogFragment;
import com.mxarcher.biue.web.ReqBody;

/**
 * @Author: MXArcher Lee
 * @Date: 2022/4/21
 */
public class HandlingFragment extends Fragment {
    private static final String TAG = "HandlingFragment";
    ConfigViewModel configViewModel;
    HandlingViewModel handlingViewModel;
    FragmentHandlingBinding binding;
    HandlingListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHandlingBinding.inflate(inflater, container, false);
        configViewModel = new ViewModelProvider(requireActivity()).get(ConfigViewModel.class);
        handlingViewModel = new ViewModelProvider(requireActivity()).get(HandlingViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.fragmentHandlingFab.setOnClickListener(v -> showDialog());
        if (adapter == null) {
            adapter = new HandlingListAdapter(new AdapterCallback<Handling>() {
                @Override
                public void onEdit(Handling handling) {

                }

                @Override
                public void onDelete(Handling handling) {

                    ReqBody<Handling> reqBody = new ReqBody<>();
                    reqBody.setModel(handling);
                    String operator_key = getString(R.string.key_operator_name);
                    reqBody.setOperator(configViewModel.get(operator_key));
                    handlingViewModel.deleteHandling(reqBody);
                }
            });
        }
        LinearLayoutManager llm = new LinearLayoutManager(requireContext());
        llm.setStackFromEnd(true);
        llm.setReverseLayout(true);
        binding.handlingInfoList.setLayoutManager(llm);
        binding.handlingInfoList.setAdapter(adapter);
        handlingViewModel.getObservableHandlingList().observe(getViewLifecycleOwner(), handlings -> {
            adapter.setHandlingList(handlings);
        });
    }

    private void showDialog() {
        DialogFragment dialog = new HandlingFullScreenDialogFragment();
        dialog.show(getParentFragmentManager(), "tag");
    }
}