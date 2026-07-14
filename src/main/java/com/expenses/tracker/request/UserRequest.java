package com.expenses.tracker.request;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder(toBuilder = true, setterPrefix = "with")
public class UserRequest {
    private String username;
    private String phoneNumber;
}
