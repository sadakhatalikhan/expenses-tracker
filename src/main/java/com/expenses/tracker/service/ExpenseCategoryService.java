package com.expenses.tracker.service;

import com.expenses.tracker.request.ExpenseCategoryRequest;
import com.expenses.tracker.response.ExpenseCategoryResponse;

import java.util.List;

public interface ExpenseCategoryService {

    ExpenseCategoryResponse addExpenseCategory(ExpenseCategoryRequest request);
    List<ExpenseCategoryResponse> getAllExpenseCategories();

}
