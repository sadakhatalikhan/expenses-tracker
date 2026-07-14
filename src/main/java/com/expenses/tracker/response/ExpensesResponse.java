package com.expenses.tracker.response;

import com.expenses.tracker.enums.ExpenseStatus;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true, setterPrefix = "with")
public class ExpensesResponse {

    private Long expenseId;
    private String userId;
    private String name;
    private String description;
    private double amount;
    private ExpenseStatus status;
    private String expenseDate;
    private String updatedDate;
    private String createdBy;
    private String updatedBy;
}
