package com.expenses.tracker.service.auth;

import com.expenses.tracker.request.AuthRequest;
import com.expenses.tracker.response.JwtResponse;

public interface AuthService {

    /**
     * Authenticates a user based on the provided authentication request and returns a JWT response containing the authentication token and user details.
     *
     * @param request the authentication request
     * @return the JWT response containing user details and the generated token
     */
    JwtResponse authenticateUser(AuthRequest request);
}
