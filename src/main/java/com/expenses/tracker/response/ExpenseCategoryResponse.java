package com.expenses.tracker.response;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true, setterPrefix = "with")
public class ExpenseCategoryResponse {

    private long id;
    private String categoryName;
    private String categoryDescription;

}
