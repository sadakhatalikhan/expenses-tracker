package com.expenses.tracker.service;

import com.expenses.tracker.request.ExpensesRequest;
import com.expenses.tracker.response.ExpensesResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface ExpensesService {

    ExpensesResponse addExpenses(@RequestBody ExpensesRequest request);
}
