package com.expenses.tracker.service;

import com.expenses.tracker.request.UserRequest;
import com.expenses.tracker.response.UserResponse;

/**
 * Service interface for user-related operations.
 */
public interface UserService {

    /**
     * Adds a new user.
     *
     * @param userRequest the user request containing user details
     * @return the response containing the added user details
     */
    UserResponse addUser(UserRequest userRequest);
}
