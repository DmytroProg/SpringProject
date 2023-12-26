package com.example.engtutor.auth;

import com.example.engtutor.viewmodel.ErrorViewModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.InstanceAlreadyExistsException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequest request){
        try {
            var token = service.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(token);
        }
        catch(InstanceAlreadyExistsException ex){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorViewModel(ex));
        }
        catch(IllegalArgumentException ex){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorViewModel(ex));
        }
        catch(Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorViewModel("Internal Server Error"));
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequest request){
        try {
            var token = service.authenticate(request);
            return ResponseEntity.ok(token);
        }
        catch(BadCredentialsException ex){
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
