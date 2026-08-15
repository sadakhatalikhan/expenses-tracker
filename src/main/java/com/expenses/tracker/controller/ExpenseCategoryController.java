package com.expenses.tracker.controller;

import com.expenses.tracker.request.ExpenseCategoryRequest;
import com.expenses.tracker.response.ApiResponse;
import com.expenses.tracker.service.ExpenseCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/expenses-category")
public class ExpenseCategoryController {

    private final ExpenseCategoryService expenseCategoryService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addExpenseCategory(@RequestBody ExpenseCategoryRequest request) {
        return ResponseEntity.ok(ApiResponse.builder()
                .withMessage("Expense category added successfully")
                .withData(expenseCategoryService.addExpenseCategory(request))
                .build()
        );
    }

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse> getAllExpenseCategories() {
        return ResponseEntity.ok(ApiResponse.builder()
                .withMessage("Expense categories retrieved successfully")
                .withData(expenseCategoryService.getAllExpenseCategories())
                .build()
        );
    }
}
