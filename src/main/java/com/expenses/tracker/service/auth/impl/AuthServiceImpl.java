package com.expenses.tracker.service.auth.impl;

import com.expenses.tracker.model.UserModel;
import com.expenses.tracker.repository.UserRepository;
import com.expenses.tracker.request.AuthRequest;
import com.expenses.tracker.response.JwtResponse;
import com.expenses.tracker.security.JwtService;
import com.expenses.tracker.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.expenses.tracker.mappers.UserDetailsMapper.jwtResponseMapper;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    /**
     * Authenticates a user based on the provided authentication request. It verifies the user's credentials and generates a JWT token if the authentication is successful. The method returns a JwtResponse containing user details and the generated token.
     *
     * @param authRequest the authentication request containing the user's phone number and password
     * @return the JwtResponse containing user details and the generated JWT token
     * @throws UsernameNotFoundException if the user is not found or the authentication fails
     */
    @Override
    public JwtResponse authenticateUser(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getPhoneNumber(), authRequest.getPassword()));
        Optional<UserModel> userModel = userRepository.findByPhoneNumber(authRequest.getPhoneNumber());
        if (!authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("invalid user request !");
        }
        return jwtResponseMapper(userModel.get(), jwtService.generateToken(authRequest.getPhoneNumber()));
    }
}
