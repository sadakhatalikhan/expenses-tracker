package com.expenses.tracker.repository;

import com.expenses.tracker.enums.ExpenseStatus;
import com.expenses.tracker.model.ExpensesModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing expenses in the MongoDB database.
 * This interface extends the MongoRepository interface, providing CRUD operations for ExpensesModel entities.
 * The ExpensesModel class represents the structure of the expenses data stored in the database.
 * By extending MongoRepository, this interface inherits methods for saving, finding, updating, and deleting expenses.
 * The @Repository annotation indicates that this interface is a Spring Data repository, allowing Spring to
 * automatically implement the necessary methods for interacting with the database.
 */
@Repository
public interface ExpensesRepository extends MongoRepository<ExpensesModel, Long> {

    Optional<List<ExpensesModel>> findAllByUserId(String userId);

    Optional<List<ExpensesModel>> findAllByStatus(ExpenseStatus status);

    Optional<ExpensesModel> findExpensesById(String expenseId);

    Optional<List<ExpensesModel>> findAllByStatusAndUserId(ExpenseStatus status, String userId);
}
