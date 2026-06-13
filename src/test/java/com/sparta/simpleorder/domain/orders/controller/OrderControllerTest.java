package com.sparta.simpleorder.domain.orders.controller;

import com.sparta.simpleorder.domain.orders.dto.request.CreateRequestDto;
import com.sparta.simpleorder.domain.orders.dto.request.UpdateRequestDto;
import com.sparta.simpleorder.domain.orders.dto.response.CreateResponseDto;
import com.sparta.simpleorder.domain.orders.dto.response.GetListResponseDto;
import com.sparta.simpleorder.domain.orders.dto.response.GetOneResponseDto;
import com.sparta.simpleorder.domain.orders.dto.response.UpdateResponseDto;
import com.sparta.simpleorder.domain.orders.entity.Order;
import com.sparta.simpleorder.domain.orders.entity.OrderStatus;
import com.sparta.simpleorder.domain.orders.service.OrderService;
import com.sparta.simpleorder.domain.products.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Test
    @DisplayName("주문단건조회_성공")
    void getOne() throws Exception {
        Long orderId = 1L;
        Long productId = 1L;
        GetOneResponseDto response = new GetOneResponseDto(
                orderId,
                productId,
                "name",
                1,
                new BigDecimal(1000),
                OrderStatus.ORDERED,
                LocalDateTime.now()
        );
        given(orderService.getOne(orderId)).willReturn(response);
        mockMvc.perform(
                        get("/orders/{id}", orderId)
                                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId));

        assertThat(response.id()).isEqualTo(orderId);
        assertThat(response.productId()).isEqualTo(productId);
        assertThat(response.productName()).isEqualTo("name");
        assertThat(response.quantity()).isEqualTo(1);
        assertThat(response.totalPrice()).isEqualTo(new BigDecimal(1000));
        assertThat(response.status()).isEqualTo(OrderStatus.ORDERED);

        verify(orderService).getOne(orderId);
    }

    @Test
    @DisplayName("주문전체조회_성공")
    void getList() throws Exception {
        int page = 0;
        int size = 10;
        Long orderId = 1L;
        Long productId = 1L;

        GetListResponseDto items = new GetListResponseDto(
                orderId,
                productId,
                "name",
                1,
                new BigDecimal(1000),
                OrderStatus.ORDERED,
                LocalDateTime.now()
        );
        Pageable pageable = PageRequest.of(page, size);

        Page<GetListResponseDto> response = new PageImpl<>(List.of(items), pageable, 1);
        given(orderService.getList(any(Pageable.class))).willReturn(response);

        mockMvc.perform(
                        get("/orders")
                                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(orderId))
                .andExpect(jsonPath("$.content[0].productId").value(productId))
                .andExpect(jsonPath("$.content[0].productName").value("name"))
                .andExpect(jsonPath("$.content[0].quantity").value(1))
                .andExpect(jsonPath("$.content[0].totalPrice").value(new BigDecimal(1000)))
                .andExpect(jsonPath("$.content[0].status").value("ORDERED"));

        verify(orderService).getList(any(Pageable.class));
    }

    @Test
    @DisplayName("주문수정_성공")
    void update() throws Exception {
        Long orderId = 1L;

        UpdateRequestDto request = new UpdateRequestDto(1, OrderStatus.ORDERED);
        UpdateResponseDto response = new UpdateResponseDto(
                orderId
        );
        given(orderService.update(any(UpdateRequestDto.class), anyLong())).willReturn(response);
        mockMvc.perform(
                        patch("/orders/{id}", orderId)
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId));
        verify(orderService).update(any(UpdateRequestDto.class), anyLong());
    }

    @Test
    @DisplayName("주문취소")
    void isDelete() throws Exception {
        Long orderId = 1L;
        mockMvc.perform(
                delete("/orders/{id}", orderId)
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().isNoContent());
        verify(orderService).delete(orderId);
    }
}