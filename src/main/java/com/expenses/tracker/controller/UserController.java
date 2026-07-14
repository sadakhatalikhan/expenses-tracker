package com.expenses.tracker.controller;

import com.expenses.tracker.request.UserRequest;
import com.expenses.tracker.response.ApiResponse;
import com.expenses.tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserController is a REST controller that handles user-related API requests. It provides an endpoint to add a new user to the system.
 * The controller uses the UserService to perform the business logic of adding a user and returns an ApiResponse containing the result of the operation.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Handles the HTTP POST request to add a new user. It accepts a UserRequest object in the request body, which contains the details of the user to be added.
     *
     * @param userRequest The request payload containing user details such as name, email, and other relevant information.
     * @return ResponseEntity containing an ApiResponse with a success message and the added user data.
     */
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addUser(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(ApiResponse.builder()
                .withMessage("User added successfully")
                .withData(userService.addUser(userRequest))
                .build());
    }
}
