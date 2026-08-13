package com.expenses.tracker.service.impl;

import com.expenses.tracker.model.ExpenseCategoryModel;
import com.expenses.tracker.model.SequenceGeneratorService;
import com.expenses.tracker.repository.ExpenseCategoryRepository;
import com.expenses.tracker.request.ExpenseCategoryRequest;
import com.expenses.tracker.response.ExpenseCategoryResponse;
import com.expenses.tracker.service.ExpenseCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.expenses.tracker.mappers.ExpenseCategoryMapper.*;

@Service
@RequiredArgsConstructor
public class ExpenseCategoryServiceImpl implements ExpenseCategoryService {

    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    @Override
    public ExpenseCategoryResponse addExpenseCategory(ExpenseCategoryRequest request) {
        expenseCategoryRepository.findByCategoryName(request.getCategoryName()).ifPresent(expenseCategory -> {
            throw new RuntimeException("Expense category with name " + request.getCategoryName() + " already exists.");
        });

       ExpenseCategoryModel categoryModel =  toModelCategoryMapper(request).toBuilder()
               .withCategoryId(sequenceGeneratorService.generateSequence("expense_category_sequence"))
               .build();

        return toCategoryResponseMapper(expenseCategoryRepository.save(categoryModel));
    }
}
