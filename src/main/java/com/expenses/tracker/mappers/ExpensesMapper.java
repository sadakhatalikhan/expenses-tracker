package com.expenses.tracker.mappers;

import com.expenses.tracker.enums.ExpenseStatus;
import com.expenses.tracker.model.ExpensesModel;
import com.expenses.tracker.request.ExpensesRequest;
import com.expenses.tracker.response.ExpensesResponse;

import java.time.LocalDateTime;

import static com.expenses.tracker.utils.AppUtils.getISTDateFormatted;

/**
 * The ExpensesMapper class provides static methods to map between ExpensesRequest, ExpensesModel, and ExpensesResponse objects.
 * It is used to convert request payloads into model objects for persistence and to convert model objects into response payloads for API responses.
 * This class helps in maintaining a clean separation between the different layers of the application by handling the transformation of data between them.
 */
public class ExpensesMapper {

    /**
     * Maps an ExpensesRequest object to an ExpensesModel object. It sets the status to PENDING and initializes the created and updated dates to the current time.
     *
     * @param request ExpenseRequest object containing the details of the expense to be added.
     * @return ExpenseModel object ready for persistence in the database.
     */
    public static ExpensesModel toModelMapper(ExpensesRequest request) {
        LocalDateTime now = LocalDateTime.now();
        return ExpensesModel.builder()
                .withId(request.getExpenseId())
                .withUserId(request.getUserId())
                .withName(request.getName())
                .withAmount(request.getAmount())
                .withDescription(request.getDescription())
                .withStatus(ExpenseStatus.PENDING)
                .withCreatedDate(now)
                .withUpdatedDate(now)
                .build();
    }

    /**
     * Maps an ExpensesModel object to an ExpensesResponse object. It formats the created and updated dates to IST format for the response.
     *
     * @param expensesModel ExpenseModel object containing the details of the expense.
     * @return ExpensesResponse object ready for API response.
     */
    public static ExpensesResponse toResponseMapper(ExpensesModel expensesModel) {
        return ExpensesResponse.builder()
                .withExpenseId(expensesModel.getId())
                .withUserId(expensesModel.getUserId())
                .withName(expensesModel.getName())
                .withDescription(expensesModel.getDescription())
                .withAmount(expensesModel.getAmount())
                .withStatus(expensesModel.getStatus())
                .withExpenseDate(getISTDateFormatted(expensesModel.getCreatedDate()))
                .withModifiedDate(getISTDateFormatted(expensesModel.getUpdatedDate()))
                .withCreatedBy(expensesModel.getCreatedBy())
                .withUpdatedBy(expensesModel.getUpdatedBy())
                .build();
    }
}
