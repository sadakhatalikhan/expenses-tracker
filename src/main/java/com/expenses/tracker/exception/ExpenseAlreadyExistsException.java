package com.expenses.tracker.exception;

public class ExpenseAlreadyExistsException extends RuntimeException {
    public ExpenseAlreadyExistsException(String message) {
        super(message);
    }
}
