package com.mxarcher.biue.views.upload.dialog;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.mxarcher.biue.R;
import com.mxarcher.biue.databinding.FragmentUserFullScreenDialogBinding;
import com.mxarcher.biue.models.User;
import com.mxarcher.biue.viewmodels.ConfigViewModel;
import com.mxarcher.biue.viewmodels.UserViewModel;
import com.mxarcher.biue.web.ReqBody;

import java.util.Objects;

// 除非只有super 不然一般别删掉自动生成的
public class UserFullScreenDialogFragment extends DialogFragment {
    FragmentUserFullScreenDialogBinding binding;

    private ConfigViewModel configViewModel;
    private UserViewModel userViewModel;

    public static UserFullScreenDialogFragment newInstance() {
        return new UserFullScreenDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogTheme);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserFullScreenDialogBinding.inflate(inflater, container, false);
        configViewModel = new ViewModelProvider(requireActivity()).get(ConfigViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            User user = new Gson().fromJson(getArguments().getString("info"), User.class);
            binding.userDialogSetComments.setText(user.getComments());
            if (user.isSex()) {
                binding.userDialogSelectGender.check(R.id.user_dialog_gender_man);
            } else {
                binding.userDialogSelectGender.check(R.id.user_dialog_gender_woman);
            }
            binding.userDialogSetName.setText(user.getName());
            binding.userDialogSetHeight.setText(String.valueOf(user.getHeight()));
            binding.userDialogSetWeight.setText(String.valueOf(user.getWeight()));
        }
        binding.fragmentHandleFullToolbar.setNavigationOnClickListener(v -> dismiss());


        binding.userDialogConfirm.setOnClickListener(v -> {
            User user = new User();
            boolean sex = binding.userDialogSelectGender.getCheckedRadioButtonId() == R.id.user_dialog_gender_man;
            user.setSex(sex);
            user.setName(Objects.requireNonNull(binding.userDialogSetName.getText()).toString());
            user.setWeight(Float.parseFloat(Objects.requireNonNull(binding.userDialogSetWeight.getText()).toString()));
            user.setHeight(Float.parseFloat(Objects.requireNonNull(binding.userDialogSetHeight.getText()).toString()));
            Editable x = binding.userDialogSetComments.getText();
            if (x != null) {
                user.setComments(x.toString());
            }
            ReqBody<User> reqBody = new ReqBody<>();
            reqBody.setModel(user);
            String operator_key = getString(R.string.key_operator_name);
            reqBody.setOperator(configViewModel.get(operator_key));
            if (getArguments() != null) {
                userViewModel.updateUser(reqBody);
            } else {
                userViewModel.addUser(reqBody);
            }
            dismiss();

        });
    }
}