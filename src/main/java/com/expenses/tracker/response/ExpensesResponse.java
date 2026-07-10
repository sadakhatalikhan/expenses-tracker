package com.expenses.tracker.response;

import com.expenses.tracker.enums.ExpenseStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true, setterPrefix = "with")
public class ExpensesResponse {

    private Long expenseId;
    private String name;
    private String description;
    private double amount;
    private ExpenseStatus status;
    private String expenseDate;
    private String modifiedDate;
    private String createdBy;
    private String updatedBy;
}
