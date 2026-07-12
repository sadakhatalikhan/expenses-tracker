package com.expenses.tracker.service.impl;

import com.expenses.tracker.exception.ExpenseAlreadyExistsException;
import com.expenses.tracker.mappers.ExpensesMapper;
import com.expenses.tracker.repository.ExpensesRepository;
import com.expenses.tracker.request.ExpensesRequest;
import com.expenses.tracker.request.UpdateExpenseStatus;
import com.expenses.tracker.response.ExpensesResponse;
import com.expenses.tracker.service.ExpensesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.expenses.tracker.mappers.ExpensesMapper.toModelMapper;
import static com.expenses.tracker.mappers.ExpensesMapper.toResponseMapper;

/**
 * Implementation of the ExpensesService interface that provides methods to manage expenses in the system.
 * This service interacts with the ExpensesRepository to perform CRUD operations on expenses.
 * It uses the ExpensesMapper to convert between request/response objects and the underlying model.
 * The addExpenses method checks for the existence of an expense with the same ID before adding a new expense, throwing a RuntimeException if a duplicate is found.
 */
@Service
@RequiredArgsConstructor
public class ExpensesServiceImpl implements ExpensesService {

    private final ExpensesRepository expensesRepository;

    /**
     * Adds a new expense to the system. If an expense with the same ID already exists, it throws a RuntimeException.
     *
     * @param request RequestPayload
     * @return ExpensesResponse
     */
    @Override
    public ExpensesResponse addExpenses(ExpensesRequest request) {
        expensesRepository.findById(request.getExpenseId()).ifPresent(expense -> {
            throw new ExpenseAlreadyExistsException("Expense with ID " + request.getExpenseId() + " already exists.");
        });
        return ExpensesMapper.toResponseMapper(
                expensesRepository.save(toModelMapper(request))
        );
    }


    /**
     * Updates the status of an existing expense in the system. If the expense with the given ID does not exist, it throws an IllegalArgumentException.
     *
     * @param request RequestPayload
     * @return ExpensesResponse
     */
    @Override
    public ExpensesResponse updateExpenseStatus(UpdateExpenseStatus request) {
        return toResponseMapper(
                expensesRepository.findById(request.getExpenseId())
                        .map(expense -> expensesRepository.save(expense.toBuilder()
                                .withStatus(request.getStatus())
                                .withUpdatedDate(java.time.LocalDateTime.now())
                                .build()))
                        .orElseThrow(() -> new IllegalArgumentException("Expense with ID " + request.getExpenseId() + " not found.")));
    }
}
