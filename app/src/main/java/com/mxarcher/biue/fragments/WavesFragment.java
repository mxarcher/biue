package com.mxarcher.biue.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mxarcher.biue.databinding.WavesFragmentBinding;
import com.mxarcher.biue.service.bluetooth.ConnectService;
import com.mxarcher.biue.viewmodels.WavesViewModel;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class WavesFragment extends Fragment {
    private static final String TAG = "WavesFragment";
    // 注意这里Size改的话要和ConnectService里的一起改
    private final int FMaxSize = 1024;
    // list 是互斥资源，上读写锁
    private final List<Short> tList = new ArrayList<>();
    private final List<Short> fList = new ArrayList<>();
    private final List<Double> ffList = new ArrayList<Double>();
    private final ReentrantReadWriteLock tListLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock fListLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock ffListLock = new ReentrantReadWriteLock();

    WavesFragmentBinding binding;
    ConnectService service;
    private String dir;
    private WavesViewModel mViewModel;

    public static WavesFragment newInstance() {
        return new WavesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = WavesFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initService();
    }

    private void initService() {
        this.binding.time.setTList(tList, tListLock);
        this.binding.freq.setFList(ffList, ffListLock);
        if (service == null) {
            service = ConnectService.getInstance(requireContext());
        }
        if (dir == null) {
            dir = requireContext().getFilesDir().toString();
        }
        binding.pressToStartService.setOnClickListener(view -> {
            service.start(tList, tListLock, fList, fListLock, dir);
        });
        binding.pressToStopService.setOnClickListener(view -> {
            service.stop();
        });
        new Thread(() -> {
            FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
            Complex[] result;
            double[] temp = new double[this.FMaxSize];
            while (true) {
                if (fList.size() == this.FMaxSize) {
                    Log.d(TAG, "initService: ");
                    fListLock.readLock().lock();
                    for (int i = 0; i < temp.length; i++) {
                        temp[i] = fList.get(i).doubleValue();
                    }
                    result = fft.transform(temp, TransformType.FORWARD);

                    ffListLock.writeLock().lock();
                    for (int i = 0; i < temp.length; i++) {
                        ffList.add(result[i].abs() / temp.length);
                        if (ffList.size() > (this.FMaxSize / 2)) {
                            ffList.remove(0);
                        }
                    }
                    ffListLock.writeLock().unlock();
                    fListLock.readLock().unlock();
                }
            }
        }).start();
    }

}