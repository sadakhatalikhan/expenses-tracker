package com.expenses.tracker.service.user.impl;

import com.expenses.tracker.mappers.UserMapper;
import com.expenses.tracker.model.SequenceGeneratorService;
import com.expenses.tracker.model.UserModel;
import com.expenses.tracker.repository.UserRepository;
import com.expenses.tracker.request.UserRequest;
import com.expenses.tracker.response.UserResponse;
import com.expenses.tracker.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.expenses.tracker.AppConstants.USER_SEQUENCE_NAME;
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

    /**
     * Adds a new user to the system. It generates a unique user ID using the SequenceGeneratorService and saves the user details in the database.
     *
     * @param userRequest the user request containing user details
     * @return the response containing the added user details
     */
    @Override
    public UserResponse addUser(UserRequest userRequest) {

        // avoid duplicate user creation with mobile number
        userRepository.findByPhoneNumber(userRequest.phoneNumber()).ifPresent(user -> {
            throw new RuntimeException("User with phone number " + userRequest.phoneNumber() + " already exists.");
        });
        // check the username to avoid duplicate
        userRepository.findByUsername(userRequest.username()).ifPresent(user -> {
            throw new RuntimeException("User with username " + userRequest.username() + " already exists.");
        });

        UserModel userModel = toUserModel(userRequest, encoder.encode(userRequest.password())).toBuilder()
                .withUserId(sequenceGeneratorService.generateSequence(USER_SEQUENCE_NAME))
                .build();
        return toUserResponse(userRepository.save(userModel));
    }

    /**
     * Retrieves a user by their ID. It fetches the user details from the database and returns a UserResponse object.
     *
     * @param id the ID of the user to retrieve
     * @return the response containing the user details
     * @throws RuntimeException if the user with the specified ID is not found
     */
    @Override
    public UserResponse getUserById(Long id) {
        return userRepository.findByUserId(id)
                .map(UserMapper::toUserResponse)
                .orElseThrow(() -> new RuntimeException("User with ID " + id + " not found."));
    }
}
