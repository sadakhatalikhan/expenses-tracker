package com.expenses.tracker.controller.user;

import com.expenses.tracker.request.UserRequest;
import com.expenses.tracker.response.ApiResponse;
import com.expenses.tracker.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Handles the HTTP GET request to retrieve a user by their ID. It accepts a user ID as a path variable and returns the corresponding user details.
     *
     * @param id The ID of the user to be retrieved.
     * @return ResponseEntity containing an ApiResponse with a success message and the retrieved user data.
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.builder()
                .withMessage("User retrieved successfully")
                .withData(userService.getUserById(id))
                .build());
    }
}
