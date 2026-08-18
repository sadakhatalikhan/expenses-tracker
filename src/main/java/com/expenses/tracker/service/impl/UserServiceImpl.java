package com.expenses.tracker.service.impl;

import com.expenses.tracker.model.SequenceGeneratorService;
import com.expenses.tracker.model.UserModel;
import com.expenses.tracker.repository.UserRepository;
import com.expenses.tracker.request.AuthRequest;
import com.expenses.tracker.request.UserRequest;
import com.expenses.tracker.response.JwtResponse;
import com.expenses.tracker.response.UserResponse;
import com.expenses.tracker.security.JwtService;
import com.expenses.tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.expenses.tracker.AppConstants.USER_SEQUENCE_NAME;
import static com.expenses.tracker.mappers.UserDetailsMapper.jwtResponseMapper;
import static com.expenses.tracker.mappers.UserMapper.toUserModel;
import static com.expenses.tracker.mappers.UserMapper.toUserResponse;

/**
 * Implementation of the UserService interface that provides methods to manage users in the system.
 * This service interacts with the UserRepository to perform CRUD operations on users.
 * It uses the ExpensesMapper to convert between request/response objects and the underlying model.
 * The addUser method generates a unique user ID using the SequenceGeneratorService before saving the user
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Adds a new user to the system. It generates a unique user ID using the SequenceGeneratorService and saves the user details in the database.
     *
     * @param userRequest the user request containing user details
     * @return the response containing the added user details
     */
    @Override
    public UserResponse addUser(UserRequest userRequest) {

        // avoid duplicate user creation with mobile number
        userRepository.findByPhoneNumber(userRequest.getPhoneNumber()).ifPresent(user -> {
            throw new RuntimeException("User with phone number " + userRequest.getPhoneNumber() + " already exists.");
        });
        // check the username to avoid duplicate
        userRepository.findByUsername(userRequest.getUsername()).ifPresent(user -> {
            throw new RuntimeException("User with username " + userRequest.getUsername() + " already exists.");
        });

        UserModel userModel = toUserModel(userRequest, encoder.encode(userRequest.getPassword())).toBuilder()
                .withUserId(sequenceGeneratorService.generateSequence(USER_SEQUENCE_NAME))
                .build();
        return toUserResponse(userRepository.save(userModel));
    }

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
