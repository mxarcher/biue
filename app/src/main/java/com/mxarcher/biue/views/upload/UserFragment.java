package com.mxarcher.biue.views.upload;

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

import com.google.gson.Gson;
import com.mxarcher.biue.R;
import com.mxarcher.biue.adapters.UserListAdapter;
import com.mxarcher.biue.databinding.FragmentUserBinding;
import com.mxarcher.biue.models.AdapterCallback;
import com.mxarcher.biue.models.User;
import com.mxarcher.biue.viewmodels.ConfigViewModel;
import com.mxarcher.biue.viewmodels.UserViewModel;
import com.mxarcher.biue.views.upload.dialog.UserFullScreenDialogFragment;
import com.mxarcher.biue.web.ReqBody;

public class UserFragment extends Fragment {
    private static final String TAG = "UserFragment";
    FragmentUserBinding binding;
    UserViewModel userViewModel;
    ConfigViewModel configViewModel;
    UserListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(inflater, container, false);
        // 获取ConfigViewModel
        configViewModel = new ViewModelProvider(requireActivity()).get(ConfigViewModel.class);
        // 获取UserViewModel
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fragmentUserFab.setOnClickListener(v -> showDialog(null));
        if (adapter == null) {
            adapter = new UserListAdapter(new AdapterCallback<User>() {
                @Override
                public void onEdit(User user) {
                    Bundle bundle = new Bundle();
                    String pass = new Gson().toJson(user);
                    bundle.putString("info", pass);
                    showDialog(bundle);
                }

                @Override
                public void onDelete(User user) {
                    ReqBody<User> reqBody = new ReqBody<>();
                    reqBody.setModel(user);
                    String operator_key = getString(R.string.key_operator_name);
                    reqBody.setOperator(configViewModel.get(operator_key));
                    userViewModel.deleteUser(reqBody);
                }
            });
        }
        binding.usersInfoList.setAdapter(adapter);

        userViewModel.getObservableUserList().observe(getViewLifecycleOwner(), userList -> {
            adapter.setUserList(userList);
            Log.d(TAG, "onViewCreated: " + userList);
        });

    }

    public void showDialog(Bundle bundle) {
        DialogFragment dialog = UserFullScreenDialogFragment.newInstance();
        if (bundle != null) {
            dialog.setArguments(bundle);
        }
        dialog.show(getParentFragmentManager(), "tag");
    }

}