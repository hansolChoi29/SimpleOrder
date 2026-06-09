package com.sparta.simpleorder.domain.products.controller;

import com.sparta.simpleorder.domain.products.dto.request.CreateRequestDto;
import com.sparta.simpleorder.domain.products.dto.response.CreateResponseDto;
import com.sparta.simpleorder.domain.products.service.ProductService;
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

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {
    @InjectMocks
    ProductController controller;

    @Mock
    ProductService service;

    MockMvc mockMvc;
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("주문생성_성공")
    void create() throws Exception {
        CreateRequestDto request = new CreateRequestDto(
                "name",
                new BigDecimal(10000),
                1
        );
        given(service.create(any(CreateRequestDto.class))).willReturn(new CreateResponseDto(1L));
        mockMvc.perform(
                        post("/products")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        ArgumentCaptor<CreateRequestDto> captor = ArgumentCaptor.forClass(CreateRequestDto.class);

        verify(service).create(captor.capture());

        CreateRequestDto captorRequest = captor.getValue();
        assertThat(captorRequest.name()).isEqualTo("name");
        assertThat(captorRequest.price()).isEqualTo(new BigDecimal(10000));
        assertThat(captorRequest.stockQuantity()).isEqualTo(1);
    }
}