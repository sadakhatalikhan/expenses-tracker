package com.expenses.tracker.service.impl;

import com.expenses.tracker.model.SequenceGeneratorService;
import com.expenses.tracker.model.UserModel;
import com.expenses.tracker.repository.UserRepository;
import com.expenses.tracker.request.UserRequest;
import com.expenses.tracker.response.UserResponse;
import com.expenses.tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.expenses.tracker.AppConstants.USER_SEQUENCE_NAME;
import static com.expenses.tracker.mappers.ExpensesMapper.toUserModel;
import static com.expenses.tracker.mappers.ExpensesMapper.toUserResponse;

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

    /**
     * Adds a new user to the system. It generates a unique user ID using the SequenceGeneratorService and saves the user details in the database.
     *
     * @param userRequest the user request containing user details
     * @return the response containing the added user details
     */
    @Override
    public UserResponse addUser(UserRequest userRequest) {

        UserModel userModel = toUserModel(userRequest).toBuilder()
                .withUserId(sequenceGeneratorService.generateSequence(USER_SEQUENCE_NAME))
                .build();

        return toUserResponse(userRepository.save(userModel));
    }
}
