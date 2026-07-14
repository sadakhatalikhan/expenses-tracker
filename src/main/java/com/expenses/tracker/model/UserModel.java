package com.expenses.tracker.model;

import com.expenses.tracker.enums.UserStatus;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Document(collection = "user_info")
@Builder(toBuilder = true, setterPrefix = "with")
public class UserModel {

    @Id
    private String id;
    private Long userId;
    private String username;
    private String password;
    private String phoneNumber;
    private UserStatus userStatus;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime updatedDate;
    @CreatedBy
    private String createdBy;
    @LastModifiedBy
    private String updatedBy;
}
