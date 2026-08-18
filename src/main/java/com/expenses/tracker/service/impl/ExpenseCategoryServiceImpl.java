package com.expenses.tracker.service.impl;

import com.expenses.tracker.mappers.ExpenseCategoryMapper;
import com.expenses.tracker.model.ExpenseCategoryModel;
import com.expenses.tracker.model.SequenceGeneratorService;
import com.expenses.tracker.repository.ExpenseCategoryRepository;
import com.expenses.tracker.request.ExpenseCategoryRequest;
import com.expenses.tracker.response.ExpenseCategoryResponse;
import com.expenses.tracker.service.ExpenseCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.expenses.tracker.mappers.ExpenseCategoryMapper.*;

/**
 * Implementation of the ExpenseCategoryService interface that provides methods to manage expense categories in the system.
 * This service interacts with the ExpenseCategoryRepository to perform CRUD operations on expense categories.
 * It uses the ExpenseCategoryMapper to convert between request/response objects and the underlying model.
 * The addExpenseCategory method checks for the existence of an expense category with the same name before adding a new category, throwing a RuntimeException if a duplicate is found.
 */
@Service
@RequiredArgsConstructor
public class ExpenseCategoryServiceImpl implements ExpenseCategoryService {

    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    /**
     * Adds a new expense category to the system. If an expense category with the same name already exists, it throws a RuntimeException.
     *
     * @param request ExpenseCategoryRequest containing the details of the category to be added
     * @return ExpenseCategoryResponse containing the details of the newly added category
     */
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

    /**
     * Retrieves all expense categories from the system.
     *
     * @return List of ExpenseCategoryResponse containing the details of all expense categories
     */
    @Override
    public List<ExpenseCategoryResponse> getAllExpenseCategories() {
        return expenseCategoryRepository.findAll().stream()
                .map(ExpenseCategoryMapper::toCategoryResponseMapper)
                .toList();
    }
}
