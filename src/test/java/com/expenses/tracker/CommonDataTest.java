package com.expenses.tracker;

import com.expenses.tracker.enums.UserStatus;
import com.expenses.tracker.request.UserRequest;
import com.expenses.tracker.response.UserResponse;
import org.junit.jupiter.params.provider.Arguments;

import java.time.LocalDate;
import java.util.stream.Stream;

public class CommonDataTest {

    private static Stream<Arguments> addUserTestData() {
        return Stream.of(
            Arguments.of(
                UserRequest.builder()
                    .withUsername("testUser01")
                    .withPhoneNumber("8989898989")
                    .build(),
                UserResponse.builder()
                    .withId("testUser01")
                    .withUserId(1001L)
                    .withUsername("testUser001")
                    .withPhoneNumber("8989898989")
                    .withUserStatus(UserStatus.ACTIVE)
                    .withCreatedDate(LocalDate.now().toString())
                    .withUpdatedDate(LocalDate.now().toString())
                    .withCreatedBy("TestUser")
                    .withUpdatedBy("TestUser")
                    .build()
            ),
            Arguments.of(
                    UserRequest.builder()
                            .withUsername("testUser02")
                            .withPhoneNumber("7878787878")
                            .build(),
                    UserResponse.builder()
                            .withId("testUser02")
                            .withUserId(1001L)
                            .withUsername("testUser001")
                            .withPhoneNumber("7878787878")
                            .withUserStatus(UserStatus.ACTIVE)
                            .withCreatedDate(LocalDate.now().toString())
                            .withUpdatedDate(LocalDate.now().toString())
                            .withCreatedBy("TestUser01")
                            .withUpdatedBy("TestUser01")
                            .build()
            )
        );
    }
}
