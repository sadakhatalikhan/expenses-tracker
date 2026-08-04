package com.expenses.tracker.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class JwtResponse {

    private String type;
    private String token;
    private String id;
    private String name;
    private String phoneNumber;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
