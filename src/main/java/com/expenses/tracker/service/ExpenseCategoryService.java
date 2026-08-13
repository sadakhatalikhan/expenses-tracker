package com.expenses.tracker.service;

import com.expenses.tracker.request.ExpenseCategoryRequest;
import com.expenses.tracker.response.ExpenseCategoryResponse;

public interface ExpenseCategoryService {

    ExpenseCategoryResponse addExpenseCategory(ExpenseCategoryRequest request);
}
