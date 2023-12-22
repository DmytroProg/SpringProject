package com.example.engtutor.controllers;

import com.example.engtutor.services.Service;
import com.example.engtutor.viewmodel.ViewModelBase;
import com.example.engtutor.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class ControllerBase<T> {
    protected final Service<T> service;

    protected ControllerBase(Service<T> service) {
        this.service = service;
    }

    protected List<ViewModelBase> getViewModels(List<T> items){
        List<ViewModelBase> viewModels = new ArrayList<>();
        for(T item : items){
            ViewModelBase itemViewModel = ViewModelFactory.createViewModel(item);
            viewModels.add(itemViewModel);
        }
        return viewModels;
    }

    protected ViewModelBase getById(Long id){
        T item = service.getById(id)
                .orElseThrow(() -> new IllegalStateException("student does not exist"));
        return ViewModelFactory.createViewModel(item);
    }
}
