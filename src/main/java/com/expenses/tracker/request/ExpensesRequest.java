package com.expenses.tracker.request;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true, setterPrefix = "with")
public class ExpensesRequest {
    private Long expenseId;
    private String name;
    private String description;
    private double amount;
}
