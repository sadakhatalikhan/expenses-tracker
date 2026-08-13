package com.expenses.tracker.mappers;

import com.expenses.tracker.enums.UserStatus;
import com.expenses.tracker.model.UserModel;
import com.expenses.tracker.request.UserRequest;
import com.expenses.tracker.response.UserResponse;

import java.time.LocalDateTime;

import static com.expenses.tracker.utils.AppUtils.getISTDateFormatted;

public class UserMapper {
    /**
     * Maps a UserRequest object to a UserModel object. It generates a random password, sets the user status to ACTIVE, and initializes the created and updated dates to the current time.
     *
     * @param userRequest  UserRequest object containing the details of the user to be added.
     * @return UserModel object ready for persistence in the database.
     */
    public static UserModel toUserModel(UserRequest userRequest, String password) {
        LocalDateTime now = LocalDateTime.now();
        return UserModel.builder()
                .withUsername(userRequest.getUsername())
                .withPassword(password)
                .withPhoneNumber(userRequest.getPhoneNumber())
                .withUserStatus(UserStatus.ACTIVE)
                .withCreatedDate(now)
                .withUpdatedDate(now)
                .build();
    }

    /**
     * Maps a UserModel object to a UserResponse object. It formats the created and updated dates to IST format for the response.
     *
     * @param userModel UserModel object containing the details of the user.
     * @return UserResponse object ready for API response.
     */
    public static UserResponse toUserResponse(UserModel userModel) {
        return UserResponse.builder()
                .withId(userModel.getId())
                .withUserId(userModel.getUserId())
                .withUsername(userModel.getUsername())
                .withPhoneNumber(userModel.getPhoneNumber())
                .withUserStatus(userModel.getUserStatus())
                .withCreatedDate(getISTDateFormatted(userModel.getCreatedDate()))
                .withUpdatedDate(getISTDateFormatted(userModel.getUpdatedDate()))
                .withCreatedBy(userModel.getCreatedBy())
                .withUpdatedBy(userModel.getUpdatedBy())
                .build();
    }
}
