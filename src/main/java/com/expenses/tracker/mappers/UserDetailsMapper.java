package com.expenses.tracker.mappers;

import com.expenses.tracker.model.UserModel;
import com.expenses.tracker.response.JwtResponse;

/**
 * This class is responsible for mapping UserModel objects to JwtResponse objects.
 * It provides a static method to create a JwtResponse from a UserModel and a JWT token.
 * The JwtResponse contains user details along with the JWT token for authentication purposes.
 * This mapping is useful for returning user information and authentication tokens in API responses after successful login or registration.
  */
public class UserDetailsMapper {

    /**
     * Maps a UserModel object and a JWT token to a JwtResponse object. This method constructs a JwtResponse containing the user's ID, phone number, username, created date, updated date, and the provided JWT token.
     *
     * @param userModel The UserModel object containing user details to be included in the JwtResponse.
     * @param token   The JWT token to be included in the JwtResponse.
     * @return The JwtResponse object containing the mapped user details and JWT token.
     */
    public static JwtResponse jwtResponseMapper(UserModel userModel, String token) {
        return JwtResponse.builder()
                .type("Bearer")
                .token(token)
                .id(userModel.getId())
                .phoneNumber(userModel.getPhoneNumber())
                .name(userModel.getUsername())
                .createdDate(userModel.getCreatedDate())
                .updatedDate(userModel.getUpdatedDate())
                .build();
    }
}
