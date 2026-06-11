package com.sparta.simpleorder.domain.products.controller;

import com.sparta.simpleorder.domain.products.dto.request.CreateRequestDto;
import com.sparta.simpleorder.domain.products.dto.response.CreateResponseDto;
import com.sparta.simpleorder.domain.products.dto.response.GetListResponse;
import com.sparta.simpleorder.domain.products.dto.response.GetOneResponse;
import com.sparta.simpleorder.domain.products.entity.Product;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    @DisplayName("상품단건조회_성공")
    void getOne() throws Exception {
        Long productId = 1L;

        GetOneResponse response = new GetOneResponse(
                productId,
                "name",
                new BigDecimal(1000),
                1
        );
        given(service.getOne(productId)).willReturn(response);

        mockMvc.perform(
                        get("/products/{id}", productId)
                                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.price").value(new BigDecimal(1000)))
                .andExpect(jsonPath("$.stockQuantity").value(1));
        verify(service).getOne(productId);
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("상품전제조회_성공")
    void getList()throws Exception{
        Long productId =  1L;
        GetListResponse items =new GetListResponse(
                productId,
                "name",
                new BigDecimal(1000),
                1
        );

        List<GetListResponse> response = List.of(items);

        given(service.getList()).willReturn(response);
        mockMvc.perform(
                get("/products")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(productId))
                .andExpect(jsonPath("$[0].name").value("name"))
                .andExpect(jsonPath("$[0].price").value(new BigDecimal(1000)))
                .andExpect(jsonPath("$[0].stockQuantity").value(1));
        verify(service).getList();
    }

}