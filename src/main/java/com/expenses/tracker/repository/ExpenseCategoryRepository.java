package com.expenses.tracker.repository;

import com.expenses.tracker.model.ExpenseCategoryModel;
import com.expenses.tracker.request.ExpenseCategoryRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpenseCategoryRepository extends MongoRepository<ExpenseCategoryModel, Long> {

    Optional<ExpenseCategoryRequest> findByCategoryName(String categoryName);
}
