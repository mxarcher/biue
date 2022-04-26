package com.mxarcher.biue.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.mxarcher.biue.models.Collection;
import com.mxarcher.biue.repositories.CollectionRepository;
import com.mxarcher.biue.web.ReqBody;

import java.util.List;

/**
 * @Author: MXArcher Lee
 * @Date: 2022/4/25 7:48
 * @Description:
 */
public class CollectionViewModel extends AndroidViewModel {
    private CollectionRepository repository = CollectionRepository.getInstance();

    public CollectionViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<Collection>> getObservableCollectionList() {
        return repository.getCollectionList();
    }

    public List<Collection> getCollectionList() {
        return repository.getCollectionList().getValue();
    }

    public void addCollection(ReqBody<Collection> reqBody) {
        repository.addCollection(reqBody);
    }

    public void deleteCollection(ReqBody<Collection> reqBody) {
        repository.deleteCollection(reqBody);
    }
}
