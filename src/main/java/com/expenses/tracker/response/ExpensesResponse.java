package com.expenses.tracker.response;

import com.expenses.tracker.enums.ExpenseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true, setterPrefix = "with")
public class ExpensesResponse {

    private Long expenseId;
    private Long userId;
    private String name;
    private String description;
    private double amount;
    private ExpenseStatus status;
    private ExpenseCategoryResponse expenseCategoryResponse;
    private String expenseDate;
    private String updatedDate;
    private String createdBy;
    private String updatedBy;
}
