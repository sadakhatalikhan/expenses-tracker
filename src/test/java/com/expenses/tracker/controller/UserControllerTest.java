package com.expenses.tracker.controller;

import com.expenses.tracker.CommonDataTest;
import com.expenses.tracker.enums.UserStatus;
import com.expenses.tracker.request.UserRequest;
import com.expenses.tracker.response.UserResponse;
import com.expenses.tracker.security.JwtAuthFilter;
import com.expenses.tracker.security.JwtService;
import com.expenses.tracker.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
class UserControllerTest extends CommonDataTest {

    private static final String ADD_USER_API = "/api/v1/users/add";

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @MockitoBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }


    @ParameterizedTest
    @MethodSource("addUserTestData")
    void addUser_200Ok_test(UserRequest request, UserResponse expectedResponse) throws Exception {
        when(userService.addUser(any())).thenReturn(expectedResponse);

        mockMvc.perform(post(ADD_USER_API)
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(jsonPath("$.id").value(expectedResponse.getId()))
                .andExpect(jsonPath("$.username").value(expectedResponse.getUsername()))
                .andExpect(jsonPath("$.phoneNumber").value(expectedResponse.getPhoneNumber()));

        verify(userService, times(2)).addUser(any(UserRequest.class));
    }
}