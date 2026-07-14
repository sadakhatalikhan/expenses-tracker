package com.expenses.tracker.request;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CommonsRequest {
    protected Long expenseId;
    protected Long userId;
}
