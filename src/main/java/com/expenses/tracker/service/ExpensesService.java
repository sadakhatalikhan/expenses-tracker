package com.expenses.tracker.service;

import com.expenses.tracker.request.ExpensesRequest;
import com.expenses.tracker.response.ExpensesResponse;

public interface ExpensesService {

    /**
     * Adds a new expense to the system. If an expense with the same ID already exists, it throws a RuntimeException.
     *
     * @param request RequestPayload
     * @return ExpensesResponse
     */
    ExpensesResponse addExpenses(ExpensesRequest request);
}
