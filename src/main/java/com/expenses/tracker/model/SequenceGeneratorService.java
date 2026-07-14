package com.expenses.tracker.model;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Service class for generating unique sequence numbers for different entities in the system.
 * This service interacts with the MongoDB database to maintain and increment sequence values for specified sequence names.
 * It uses the MongoOperations interface to perform find-and-modify operations on the database.
 * The generateSequence method retrieves the current sequence value for a given sequence name, increments it by one, and returns the updated value. If the sequence name does not exist, it creates a new entry with an initial value of 1.
 * This service is typically used to generate unique identifiers for entities such as users, expenses, or other records that require sequential numbering.
 */
@Service
@RequiredArgsConstructor
public class SequenceGeneratorService {

    private final MongoOperations mongoOperations;

    /**
     * Generates a unique sequence number for the specified sequence name. If the sequence name does not exist in the database, it creates a new entry with an initial value of 1. The method retrieves the current sequence value, increments it by one, and returns the updated value.
     *
     * @param sequenceName the name of the sequence for which to generate a unique number
     * @return the updated sequence value after incrementing
     */
    public long generateSequence(String sequenceName) {

        Query query = new Query(Criteria.where("_id").is(sequenceName));

        Update update = new Update().inc("sequenceValue", 1);

        FindAndModifyOptions options =
                FindAndModifyOptions.options()
                        .returnNew(true)
                        .upsert(true);

        DatabaseSequence sequence =
                mongoOperations.findAndModify(
                        query,
                        update,
                        options,
                        DatabaseSequence.class);

        return Objects.requireNonNull(sequence).getSequenceValue();
    }
}
