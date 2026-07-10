package com.expenses.tracker.response;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true, setterPrefix = "with")
public class ApiResponse {
    private String message;
    private Object data;
}
