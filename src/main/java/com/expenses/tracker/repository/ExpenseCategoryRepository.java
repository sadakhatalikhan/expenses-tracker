package com.expenses.tracker.repository;

import com.expenses.tracker.model.ExpenseCategoryModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpenseCategoryRepository extends MongoRepository<ExpenseCategoryModel, Long> {

    Optional<ExpenseCategoryModel> findByCategoryName(String categoryName);
}
