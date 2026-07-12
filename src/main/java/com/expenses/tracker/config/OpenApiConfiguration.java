package com.expenses.tracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI expenseOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Expense Tracker API")
                        .description("Expense Management Microservice")
                        .version("v1.0"));
    }
}