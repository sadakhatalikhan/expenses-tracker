package com.expenses.tracker.controller.auth;

import com.expenses.tracker.request.AuthRequest;
import com.expenses.tracker.response.ApiResponse;
import com.expenses.tracker.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/user")
public class AuthenticationController {

    private final AuthService authService;

    /**
     * Authenticate user
     *
     * @param authRequest Auth userRequest
     * @return Response Entity of Type ApiResponse
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequest authRequest) {

        return ResponseEntity.ok(
            ApiResponse.builder()
                .withMessage("success")
                .withData(authService.authenticateUser(authRequest))
                .build()
        );
    }
    // Logout

    // getUserDetails By ID

    // Update User details
}
