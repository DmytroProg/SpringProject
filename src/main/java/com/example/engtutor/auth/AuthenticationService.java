package com.example.engtutor.auth;

import com.example.engtutor.config.JwtService;
import com.example.engtutor.user.Role;
import com.example.engtutor.user.User;
import com.example.engtutor.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.management.InstanceAlreadyExistsException;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    public static boolean isValidPassword(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) throws InstanceAlreadyExistsException {
        validate(request);

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new InstanceAlreadyExistsException();
        }

        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        validate(request);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }

    public void validate(RegisterRequest request){
        if(request.getFirstname() == null || request.getFirstname().isEmpty()){
            throw new IllegalArgumentException("first name must not be empty");
        }
        if(request.getLastname() == null || request.getLastname().isEmpty()){
            throw new IllegalArgumentException("last name must not be empty");
        }
        validateAuthData(request.getEmail(), request.getPassword());
    }

    public void validate(AuthenticationRequest request){
        validateAuthData(request.getEmail(), request.getPassword());
    }

    public void validateAuthData(String email, String password){
        if(email == null || email.isEmpty()){
            throw new IllegalArgumentException("email must not be empty");
        }
        if(password == null || password.isEmpty()){
            throw new IllegalArgumentException("password must not be empty");
        }
        if(!isValidPassword(password)){
            throw new IllegalArgumentException("password is incorrect");
        }
    }
}
