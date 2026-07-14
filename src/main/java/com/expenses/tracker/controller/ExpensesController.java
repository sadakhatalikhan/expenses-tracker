package com.expenses.tracker.controller;

import com.expenses.tracker.enums.ExpenseStatus;
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

    /**
     * Retrieves all expenses from the system and returns them in the response.
     *
     * @return ApiResponse object
     */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllExpenses() {
        return ResponseEntity.ok(ApiResponse.builder()
                .withMessage("Expenses loaded successfully")
                .withData(expensesService.getAllExpenses())
                .build()
        );
    }

    /**
     * Retrieves expenses for a specific user based on the provided userId and returns them in the response.
     *
     * @param userId userId
     * @return ApiResponse Object
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getExpenseByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.builder()
                .withMessage("Expenses loaded successfully")
                .withData(expensesService.getExpenseByUserId(userId))
                .build()
        );
    }

    /**
     * Retrieves expenses for a specific expenseId and returns them in the response.
     *
     * @param expenseId expenseId
     * @return ApiResponse
     */
    @GetMapping("/{expenseId}")
    public ResponseEntity<ApiResponse> getExpenseByExpenseId(@PathVariable Long expenseId) {
        return ResponseEntity.ok(ApiResponse.builder()
                .withMessage("Expenses loaded successfully")
                .withData(expensesService.getExpenseByExpenseId(expenseId))
                .build()
        );
    }

    /**
     * Retrieves expenses based on the provided status and returns them in the response.
     *
     * @param status Status
     * @return ApiResponse
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse> getExpenseByStatus(@PathVariable ExpenseStatus status) {
        return ResponseEntity.ok(ApiResponse.builder()
                .withMessage("Expenses loaded successfully")
                .withData(expensesService.getExpensesByStatus(status))
                .build()
        );
    }

    /**
     *  Retrieves expenses for a specific user based on the provided userId and status, and returns them in the response.
     *
     * @param status Status
     * @param userId UserId
     * @return ApiResponse
     */
    @GetMapping("/user/status/{userId}/{status}")
    public ResponseEntity<ApiResponse> getExpensesForUserByStatus(@PathVariable ExpenseStatus status, @PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.builder()
                .withMessage("Expenses loaded successfully")
                .withData(expensesService.getExpensesForUserByStatus(status, userId))
                .build()
        );
    }
}
