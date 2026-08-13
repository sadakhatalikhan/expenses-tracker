package com.expenses.tracker.request;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder(toBuilder = true, setterPrefix = "with")
public class ExpenseCategoryRequest {

    private String categoryName;
    private String categoryDescription;
}
