package com.mxarcher.biue.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.mxarcher.biue.models.Handling;
import com.mxarcher.biue.repositories.HandlingRepository;
import com.mxarcher.biue.service.web.ReqBody;

import java.util.List;

/**
 * @Author: MXArcher Lee
 * @Date: 2022/4/25 7:50
 * @Description:
 */
public class HandlingViewModel extends AndroidViewModel {
    private HandlingRepository repository = HandlingRepository.getInstance();

    public HandlingViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<Handling>> getObservableHandlingList(){
        return repository.getHandlingList();
    }
    public List<Handling> getHandlingList(){
        return repository.getHandlingList().getValue();
    }
    public void addHandling(ReqBody<Handling> reqBody) {
        repository.addHandling(reqBody);
    }
    public void deleteHandling(ReqBody<Handling> reqBody) {
        repository.deleteHandling(reqBody);
    }
    public void updateHandling(ReqBody<Handling> reqBody){
        repository.updateHandling(reqBody);
    }
}

