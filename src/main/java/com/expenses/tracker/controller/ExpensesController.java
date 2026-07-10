package com.expenses.tracker.controller;

import com.expenses.tracker.request.ExpensesRequest;
import com.expenses.tracker.response.ApiResponse;
import com.expenses.tracker.response.ExpensesResponse;
import com.expenses.tracker.service.ExpensesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/expenses")
public class ExpensesController {

    private final ExpensesService expensesService;

    // add expenses
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addExpenses(@RequestBody ExpensesRequest request) {
        ExpensesResponse response = expensesService.addExpenses(request);
        return ResponseEntity.ok(ApiResponse.builder()
                .withMessage("Expenses added successfully")
                .withData(response)
                .build()
        );
    }

    // update expenses

    // load All expenses

    // load expenses byId

}
