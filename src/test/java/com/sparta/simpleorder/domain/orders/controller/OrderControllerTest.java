package com.sparta.simpleorder.domain.orders.controller;

import com.sparta.simpleorder.domain.orders.dto.request.CreateRequestDto;
import com.sparta.simpleorder.domain.orders.dto.response.CreateResponseDto;
import com.sparta.simpleorder.domain.orders.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {
    @InjectMocks
    OrderController orderController;

    @Mock
    OrderService orderService;

    MockMvc mockMvc;
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("주문_성공")
    void create() throws Exception {
        Long orderId = 1L;
        Long productId = 1L;
        CreateRequestDto request = new CreateRequestDto(
                productId,
                1
        );
        given(orderService.create(any(CreateRequestDto.class))).willReturn(new CreateResponseDto(orderId));
        mockMvc.perform(
                        post("/orders")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(orderId));
        ArgumentCaptor<CreateRequestDto> captor = ArgumentCaptor.forClass(CreateRequestDto.class);

        verify(orderService).create(captor.capture());

        CreateRequestDto captorRequest = captor.getValue();
        assertThat(captorRequest.quantity()).isEqualTo(1);
    }
}