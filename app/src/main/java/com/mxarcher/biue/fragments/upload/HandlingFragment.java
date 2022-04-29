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

import com.google.gson.Gson;
import com.mxarcher.biue.R;
import com.mxarcher.biue.adapters.HandlingListAdapter;
import com.mxarcher.biue.databinding.FragmentHandlingBinding;
import com.mxarcher.biue.models.AdapterCallback;
import com.mxarcher.biue.models.Handling;
import com.mxarcher.biue.viewmodels.ConfigViewModel;
import com.mxarcher.biue.viewmodels.HandlingViewModel;
import com.mxarcher.biue.fragments.upload.dialog.HandlingFullScreenDialogFragment;
import com.mxarcher.biue.service.web.ReqBody;

/**
 * @Author: MXArcher Lee
 * @Date: 2022/4/21
 */
// TODO: 实现下拉刷新
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHandlingBinding.inflate(inflater, container, false);
        configViewModel = new ViewModelProvider(requireActivity()).get(ConfigViewModel.class);
        handlingViewModel = new ViewModelProvider(requireActivity()).get(HandlingViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.fragmentHandlingFab.setOnClickListener(v -> showDialog(null));
        if (adapter == null) {
            adapter = new HandlingListAdapter(new AdapterCallback<Handling>() {
                @Override
                public void onEdit(Handling handling) {
                    Bundle bundle = new Bundle();
                    String pass = new Gson().toJson(handling);
                    bundle.putString("info", pass);
                    showDialog(bundle);
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
            Log.d(TAG, "onViewCreated: ");
        });
    }

    private void showDialog(Bundle bundle) {
        DialogFragment dialog = new HandlingFullScreenDialogFragment();
        if (bundle != null) {
            dialog.setArguments(bundle);
        }
        dialog.show(getParentFragmentManager(), "tag");
    }
}