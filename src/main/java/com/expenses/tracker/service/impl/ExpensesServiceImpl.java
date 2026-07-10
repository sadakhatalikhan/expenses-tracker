package com.expenses.tracker.service.impl;

import com.expenses.tracker.mappers.ExpensesMapper;
import com.expenses.tracker.model.ExpensesModel;
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
        ExpensesModel expensesModel = toModelMapper(request);
        expensesModel = expensesRepository.save(expensesModel);
        return ExpensesMapper.toResponseMapper(expensesModel);
    }
}
