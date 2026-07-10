package com.expenses.tracker.repository;

import com.expenses.tracker.model.ExpensesModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpensesRepository extends MongoRepository<ExpensesModel, String> {
}
