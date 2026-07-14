package com.expenses.tracker.model;

import com.expenses.tracker.enums.ExpenseStatus;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Document(collection = "expenses_info")
@Builder(toBuilder = true, setterPrefix = "with")
public class ExpensesModel {
    @Id
    private Long id;
    private Long userId;
    private String name;
    private String description;
    private double amount;
    private ExpenseStatus status;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime updatedDate;
    @CreatedBy
    private String createdBy;
    @LastModifiedBy
    private String updatedBy;
}
