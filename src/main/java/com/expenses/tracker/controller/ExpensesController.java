package com.expenses.tracker.controller;

import com.expenses.tracker.request.ExpensesRequest;
import com.expenses.tracker.request.UpdateExpenseStatus;
import com.expenses.tracker.response.ApiResponse;
import com.expenses.tracker.response.ExpensesResponse;
import com.expenses.tracker.service.ExpensesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Expenses controller class to track the customer expenses throughout his journey.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/expenses")
public class ExpensesController {

    private final ExpensesService expensesService;

    /**
     * Add expenses into the MongoDB database.
     *
     * @param request Request Payload
     * @return ApiResponse object
     */
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addExpenses(@RequestBody ExpensesRequest request) {
        ExpensesResponse response = expensesService.addExpenses(request);
        return ResponseEntity.ok(ApiResponse.builder()
                .withMessage("Expenses added successfully")
                .withData(response)
                .build()
        );
    }

    /**
     * Updates the status of an existing expense in the system.
     *
     * @param request Request Payload
     * @return ApiResponse object
     */
    @PutMapping("/update-status")
    public ResponseEntity<ApiResponse> updateExpenseStatus(@RequestBody UpdateExpenseStatus request) {
        ExpensesResponse response = expensesService.updateExpenseStatus(request);
        return ResponseEntity.ok(ApiResponse.builder()
                .withMessage("Expenses status updated successfully")
                .withData(response)
                .build()
        );
    }
    // load expenses bases on the status


    // load expenses byId

}
