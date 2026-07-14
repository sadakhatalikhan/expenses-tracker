package com.expenses.tracker.repository;

import com.expenses.tracker.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing users in the MongoDB database.
 * This interface extends the MongoRepository interface, providing CRUD operations for UserModel entities.
 * The UserModel class represents the structure of the user data stored in the database.
 * By extending MongoRepository, this interface inherits methods for saving, finding, updating, and deleting users.
 * The @Repository annotation indicates that this interface is a Spring Data repository, allowing Spring to
 * automatically implement the necessary methods for interacting with the database.
 */
@Repository
public interface UserRepository extends MongoRepository<UserModel, Long> {
}
