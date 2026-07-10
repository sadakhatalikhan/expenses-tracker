package com.expenses.tracker.mappers;

import com.expenses.tracker.enums.ExpenseStatus;
import com.expenses.tracker.model.ExpensesModel;
import com.expenses.tracker.request.ExpensesRequest;
import com.expenses.tracker.response.ExpensesResponse;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.expenses.tracker.utils.AppUtils.getISTDateFormatted;

public class ExpensesMapper {

    public static ExpensesModel toModelMapper(ExpensesRequest request) {
        LocalDateTime now = LocalDateTime.now();
        return ExpensesModel.builder()
                .withId(UUID.randomUUID().toString())
                .withName(request.getName())
                .withAmount(request.getAmount())
                .withDescription(request.getDescription())
                .withStatus(ExpenseStatus.PENDING)
                .withCreatedDate(now)
                .withUpdatedDate(now)
                .build();
    }

    public static ExpensesResponse toResponseMapper(ExpensesModel expensesModel) {
        return ExpensesResponse.builder()
                .withId(expensesModel.getId())
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
