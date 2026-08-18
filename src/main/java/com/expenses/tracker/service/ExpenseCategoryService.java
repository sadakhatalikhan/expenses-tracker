package com.expenses.tracker.service;

import com.expenses.tracker.request.ExpenseCategoryRequest;
import com.expenses.tracker.response.ExpenseCategoryResponse;

import java.util.List;

/**
 * Service interface for managing expense categories in the system.
 * Provides methods to add a new expense category and retrieve all existing categories.
 * Implementations of this interface should handle the business logic for interacting with the underlying data store.
 */
public interface ExpenseCategoryService {

    /**
     * Adds a new expense category to the system based on the provided request.
     *
     * @param request ExpenseCategoryRequest containing the details of the category to be added
     * @return ExpenseCategoryResponse containing the details of the newly added category
     */
    ExpenseCategoryResponse addExpenseCategory(ExpenseCategoryRequest request);

    /**
     * Retrieves all expense categories from the system.
     *
     * @return List of ExpenseCategoryResponse containing the details of all categories
     */
    List<ExpenseCategoryResponse> getAllExpenseCategories();

}
