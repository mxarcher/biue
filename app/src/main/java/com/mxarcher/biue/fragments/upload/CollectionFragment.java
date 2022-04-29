package com.mxarcher.biue.fragments.upload;

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

import com.google.gson.Gson;
import com.mxarcher.biue.R;
import com.mxarcher.biue.adapters.CollectionListAdapter;
import com.mxarcher.biue.databinding.FragmentCollectionBinding;
import com.mxarcher.biue.models.AdapterCallback;
import com.mxarcher.biue.models.Collection;
import com.mxarcher.biue.viewmodels.CollectionViewModel;
import com.mxarcher.biue.viewmodels.ConfigViewModel;
import com.mxarcher.biue.fragments.upload.dialog.CollectionFullScreenDialogFragment;
import com.mxarcher.biue.service.web.ReqBody;

// TODO: 实现下拉刷新
public class CollectionFragment extends Fragment {
    private static final String TAG = "CollectionFragment";
    FragmentCollectionBinding binding;
    CollectionViewModel collectionViewModel;
    ConfigViewModel configViewModel;
    CollectionListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCollectionBinding.inflate(inflater, container, false);
        configViewModel = new ViewModelProvider(requireActivity()).get(ConfigViewModel.class);
        collectionViewModel = new ViewModelProvider(requireActivity()).get(CollectionViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (adapter == null) {
            adapter = new CollectionListAdapter(new AdapterCallback<Collection>() {
                @Override
                public void onEdit(Collection collection) {
                    Bundle bundle = new Bundle();
                    String pass = new Gson().toJson(collection);
                    bundle.putString("info", pass);
                    showDialog(bundle);
                }

                @Override
                public void onDelete(Collection collection) {
                    ReqBody<Collection> reqBody = new ReqBody<>();
                    reqBody.setModel(collection);
                    String operator_key = getString(R.string.key_operator_name);
                    reqBody.setOperator(configViewModel.get(operator_key));
                    collectionViewModel.deleteCollection(reqBody);
                }
            });
        }
        binding.fragmentCollectionFab.setOnClickListener(v -> showDialog(null));
        LinearLayoutManager llm = new LinearLayoutManager(requireContext());
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        binding.collectionInfoList.setLayoutManager(llm);
        binding.collectionInfoList.setAdapter(adapter);
        collectionViewModel.getObservableCollectionList().observe(getViewLifecycleOwner(), collections -> {
            adapter.setCollectionList(collections);
        });

    }

    private void showDialog(Bundle bundle) {
        DialogFragment dialog = new CollectionFullScreenDialogFragment();
        if (bundle != null)
            dialog.setArguments(bundle);
        dialog.show(getParentFragmentManager(), "tag");

    }
}