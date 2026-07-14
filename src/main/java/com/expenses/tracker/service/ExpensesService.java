package com.expenses.tracker.service;

import com.expenses.tracker.enums.ExpenseStatus;
import com.expenses.tracker.request.ExpensesRequest;
import com.expenses.tracker.request.UpdateExpenseStatus;
import com.expenses.tracker.response.ExpensesResponse;

import java.util.List;

public interface ExpensesService {

    /**
     * Adds a new expense to the system. If an expense with the same ID already exists, it throws a RuntimeException.
     *
     * @param request RequestPayload
     * @return ExpensesResponse
     */
    ExpensesResponse addExpenses(ExpensesRequest request);

    /**
     * Updates the status of an existing expense in the system.
     *
     * @param request RequestPayload
     * @return ExpensesResponse
     */
    ExpensesResponse updateExpenseStatus(UpdateExpenseStatus request);

    /**
     * Retrieves all expenses from the system.
     *
     * @return List of ExpensesResponse
     */
    List<ExpensesResponse> getAllExpenses();

    /**
     * Retrieves expenses for a specific user based on the provided userId and returns them in the response.
     *
     * @param userId userId
     * @return ApiResponse Object
     */
    List<ExpensesResponse> getExpenseByUserId(Long userId);

    /**
     * Retrieves expenses for a specific expenseId and returns them in the response.
     *
     * @param expenseId expenseId
     * @return ApiResponse
     */
    ExpensesResponse getExpenseByExpenseId(Long expenseId);

    /**
     * Retrieves expenses based on the provided status and returns them in the response.
     *
     * @param status Status
     * @return ApiResponse
     */
    List<ExpensesResponse> getExpensesByStatus(ExpenseStatus status);

    /**
     * Retrieves expenses for a specific user based on the provided userId and status, and returns them in the response.
     *
     * @param status Status
     * @param userId UserId
     * @return ApiResponse
     */
    List<ExpensesResponse> getExpensesForUserByStatus(ExpenseStatus status, Long userId);
}
