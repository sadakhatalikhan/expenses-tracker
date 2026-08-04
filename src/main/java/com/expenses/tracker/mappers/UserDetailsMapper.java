package com.expenses.tracker.mappers;

import com.expenses.tracker.model.UserModel;
import com.expenses.tracker.response.JwtResponse;

public class UserDetailsMapper {

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
