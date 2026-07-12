package com.expenses.tracker.request;

import com.expenses.tracker.enums.ExpenseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true, setterPrefix = "with")
public class UpdateExpenseStatus extends CommonsRequest {
    private ExpenseStatus status;
}
