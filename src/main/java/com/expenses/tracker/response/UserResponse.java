package com.expenses.tracker.response;

import com.expenses.tracker.enums.UserStatus;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder(toBuilder = true, setterPrefix = "with")
public class UserResponse {

    private String id;
    private Long userId;
    private String username;
    private String phoneNumber;
    private UserStatus userStatus;
    private String createdDate;
    private String updatedDate;
    private String createdBy;
    private String updatedBy;
}
