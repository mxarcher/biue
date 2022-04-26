package com.mxarcher.biue.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.mxarcher.biue.models.User;
import com.mxarcher.biue.repositories.UserRepository;
import com.mxarcher.biue.web.ReqBody;

import java.util.List;

/**
 * @Author: MXArcher Lee
 * @Date: 2022/4/23 10:37
 * @Description:
 */
public class UserViewModel extends AndroidViewModel {
    private final UserRepository repository = UserRepository.getInstance();

    public UserViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<User>> getObservableUserList() {
        return repository.getUserList();
    }
    public List<User> getUserList(){
        return repository.getUserList().getValue();
    }

    public void addUser(ReqBody<User> reqBody) {
        repository.addUser(reqBody);
    }

    public void deleteUser(ReqBody<User> reqBody) {
        repository.deleteUser(reqBody);
    }
    public void updateUser(ReqBody<User> reqBody){
        repository.updateUser(reqBody);
    }
}
