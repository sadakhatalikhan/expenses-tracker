package com.expenses.tracker.service;

import com.expenses.tracker.request.AuthRequest;
import com.expenses.tracker.request.UserRequest;
import com.expenses.tracker.response.JwtResponse;
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

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return the response containing the user details
     */
    UserResponse getUserById(Long id);

    /**
     * Authenticates a user based on the provided authentication request and returns a JWT response containing the authentication token and user details.
     *
     * @param request the authentication request
     * @return the JWT response containing user details and the generated token
     */
    JwtResponse authenticateUser(AuthRequest request);
}
