package com.expenses.tracker.request;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder(toBuilder = true, setterPrefix = "with")
public class ExpensesRequest extends CommonsRequest {
    private String name;
    private String description;
    private double amount;
}
