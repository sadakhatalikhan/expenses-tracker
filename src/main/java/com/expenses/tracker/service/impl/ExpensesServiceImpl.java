package com.expenses.tracker.service.impl;

import com.expenses.tracker.mappers.ExpensesMapper;
import com.expenses.tracker.repository.ExpensesRepository;
import com.expenses.tracker.request.ExpensesRequest;
import com.expenses.tracker.response.ExpensesResponse;
import com.expenses.tracker.service.ExpensesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.expenses.tracker.mappers.ExpensesMapper.toModelMapper;

@Service
@RequiredArgsConstructor
public class ExpensesServiceImpl implements ExpensesService {

    private final ExpensesRepository expensesRepository;

    @Override
    public ExpensesResponse addExpenses(ExpensesRequest request) {
        expensesRepository.findById(request.getExpenseId()).ifPresent(expense -> {
            throw new RuntimeException("Expense with ID " + request.getExpenseId() + " already exists.");
        });
        return ExpensesMapper.toResponseMapper(
                expensesRepository.save(toModelMapper(request))
        );
    }
}
