package com.expenses.tracker.request;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder(toBuilder = true, setterPrefix = "with")
public class AuthRequest {

    private String phoneNumber;
    private String password;

}
