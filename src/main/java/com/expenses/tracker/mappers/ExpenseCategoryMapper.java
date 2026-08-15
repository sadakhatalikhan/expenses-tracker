package com.expenses.tracker.mappers;

import com.expenses.tracker.model.ExpenseCategoryModel;
import com.expenses.tracker.request.ExpenseCategoryRequest;
import com.expenses.tracker.response.ExpenseCategoryResponse;

public class ExpenseCategoryMapper {

    public static ExpenseCategoryModel toModelCategoryMapper(ExpenseCategoryRequest categoryRequest) {
        return ExpenseCategoryModel.builder()
                .withCategoryName(categoryRequest.getCategoryName())
                .withCategoryDescription(categoryRequest.getCategoryDescription())
                .build();
    }

    public static ExpenseCategoryResponse toCategoryResponseMapper(ExpenseCategoryModel model) {
        return ExpenseCategoryResponse.builder()
                .withId(model.getCategoryId())
                .withCategoryName(model.getCategoryName())
                .withCategoryDescription(model.getCategoryDescription())
                .build();
    }
}
