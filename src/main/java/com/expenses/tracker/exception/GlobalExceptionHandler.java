package com.expenses.tracker.exception;

import com.expenses.tracker.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle Internal Server Exception and return a custom error response.
     *
     * @param ex RuntimeException
     * @return ResponseEntity with ApiResponse containing error message and empty data list
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleRuntime(RuntimeException ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.builder().withMessage(ex.getMessage()).withData(List.of()).build());
    }

    /**
     * Handle Expense Already Exists Exception and return a custom error response.
     *
     * @param ex ExpenseAlreadyExistsException
     * @return ResponseEntity with ApiResponse containing error message and empty data list
     */
    @ExceptionHandler(ExpenseAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleExpenseAlreadyExistsException(ExpenseAlreadyExistsException ex) {
        log.warn("Expense already exists: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.builder().withMessage(ex.getMessage()).withData(List.of()).build());
    }
}


