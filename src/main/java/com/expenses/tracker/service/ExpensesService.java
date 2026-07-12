package com.expenses.tracker.service;

import com.expenses.tracker.request.ExpensesRequest;
import com.expenses.tracker.request.UpdateExpenseStatus;
import com.expenses.tracker.response.ExpensesResponse;

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
}
