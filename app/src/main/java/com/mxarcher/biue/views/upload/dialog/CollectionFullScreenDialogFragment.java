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
import com.mxarcher.biue.adapters.UserSpinnerAdapter;
import com.mxarcher.biue.databinding.FragmentCollectionFullScreenDialogBinding;
import com.mxarcher.biue.models.Collection;
import com.mxarcher.biue.models.User;
import com.mxarcher.biue.viewmodels.CollectionViewModel;
import com.mxarcher.biue.viewmodels.ConfigViewModel;
import com.mxarcher.biue.viewmodels.UserViewModel;
import com.mxarcher.biue.web.ReqBody;

import java.util.List;
import java.util.Objects;

public class CollectionFullScreenDialogFragment extends DialogFragment {
    private static final String TAG = "CollectionFullScreenDia";
    FragmentCollectionFullScreenDialogBinding binding;
    private UserViewModel userViewModel;
    private CollectionViewModel collectionViewModel;
    private ConfigViewModel configViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogTheme);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCollectionFullScreenDialogBinding.inflate(inflater, container, false);
        configViewModel = new ViewModelProvider(requireActivity()).get(ConfigViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        collectionViewModel = new ViewModelProvider(requireActivity()).get(CollectionViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fragmentHandleFullToolbar.setNavigationOnClickListener(v -> dismiss());
        List<User> userList = userViewModel.getUserList();
        UserSpinnerAdapter userSpinnerAdapter = new UserSpinnerAdapter(requireContext(), userList);
        binding.collectionDialogSelectUser.setAdapter(userSpinnerAdapter);


        binding.collectionDialogConfirm.setOnClickListener(v -> {
            Collection collection = new Collection();
            User user = (User) binding.collectionDialogSelectUser.getSelectedItem();
            collection.setUid(user.getId());
            collection.setDataset(Objects.requireNonNull(binding.collectionDialogSetDataset.getText()).toString());
            collection.setName(Objects.requireNonNull(binding.collectionDialogSetName.getText()).toString());
            collection.setComments(Objects.requireNonNull(binding.collectionDialogSetComments.getText()).toString());
            collection.setProgram(Objects.requireNonNull(binding.collectionDialogSetProgram.getText()).toString());
            collection.setLocation(Objects.requireNonNull(binding.collectionDialogSetLocation.getText()).toString());
            ReqBody<Collection> reqBody = new ReqBody<>();
            reqBody.setModel(collection);
            String operator_key = getString(R.string.key_operator_name);
            reqBody.setOperator(configViewModel.get(operator_key));
            collectionViewModel.addCollection(reqBody);
            Log.d(TAG, "onViewCreated: " + collection);
            dismiss();
        });

    }
}