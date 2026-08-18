package com.expenses.tracker.mappers;

import com.expenses.tracker.model.ExpenseCategoryModel;
import com.expenses.tracker.request.ExpenseCategoryRequest;
import com.expenses.tracker.response.ExpenseCategoryResponse;

/**
 * The ExpenseCategoryMapper class provides static methods to convert between different representations of expense category data.
 * It includes methods to map from ExpenseCategoryRequest to ExpenseCategoryModel for persistence and from ExpenseCategoryModel to ExpenseCategoryResponse for API responses.
 * This class helps maintain a clean separation between the different layers of the application by handling the transformation of data between them.
 */
public class ExpenseCategoryMapper {

    /**
     * Maps an ExpenseCategoryRequest object to an ExpenseCategoryModel object. This method is used to convert the request payload into a model object suitable for persistence in the database.
     *
     * @param categoryRequest ExpenseCategoryRequest object containing the details of the expense category to be added.
     * @return ExpenseCategoryModel object ready for persistence in the database.
     */
    public static ExpenseCategoryModel toModelCategoryMapper(ExpenseCategoryRequest categoryRequest) {
        return ExpenseCategoryModel.builder()
                .withCategoryName(categoryRequest.getCategoryName())
                .withCategoryDescription(categoryRequest.getCategoryDescription())
                .build();
    }

    /**
     * Maps an ExpenseCategoryModel object to an ExpenseCategoryResponse object. This method is used to convert the model object into a response payload suitable for API responses.
     *
     * @param model ExpenseCategoryModel object containing the details of the expense category.
     * @return ExpenseCategoryResponse object ready for API responses.
     */
    public static ExpenseCategoryResponse toCategoryResponseMapper(ExpenseCategoryModel model) {
        return ExpenseCategoryResponse.builder()
                .withId(model.getCategoryId())
                .withCategoryName(model.getCategoryName())
                .withCategoryDescription(model.getCategoryDescription())
                .build();
    }
}
