package com.example.engtutor.controllers;

import com.example.engtutor.services.Service;
import com.example.engtutor.viewmodel.ErrorViewModel;
import com.example.engtutor.viewmodel.ViewModelBase;
import com.example.engtutor.viewmodel.ViewModelFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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

    public ResponseEntity<Object> handleResponse(Function<Object, Object> getResponse, HttpStatus status) {
        try{
            var response = getResponse.apply(null);
            return ResponseEntity.status(status).body(response);
        }
        catch(NullPointerException | BadCredentialsException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorViewModel(ex));
        }
        catch(IllegalArgumentException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorViewModel(ex));
        }
        catch(Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorViewModel("Internal Server Error"));
        }
    }
}
