package com.sparta.simpleorder.domain.products.controller;

import com.sparta.simpleorder.domain.products.dto.request.CreateRequestDto;
import com.sparta.simpleorder.domain.products.dto.request.UpdateRequestDto;
import com.sparta.simpleorder.domain.products.dto.response.CreateResponseDto;
import com.sparta.simpleorder.domain.products.dto.response.GetListResponseDto;
import com.sparta.simpleorder.domain.products.dto.response.GetOneResponseDto;
import com.sparta.simpleorder.domain.products.dto.response.UpdateResponseDto;
import com.sparta.simpleorder.domain.products.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Products", description = "상품 등록·조회·수정·삭제 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;


    @Operation(summary = "상품 생성", description = "상품명, 가격, 수량을 입력하여 생성합니다")
    @PostMapping
    public ResponseEntity<CreateResponseDto> create(
            @RequestBody CreateRequestDto request
    ) {
        CreateResponseDto response = productService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "상품 단건조회", description = "상품 ID로 특정 상품의 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<GetOneResponseDto> getOne(
            @PathVariable("id") Long id
    ) {
        GetOneResponseDto response = productService.getOne(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "상품 전체조회", description = "등록된 모든 상품을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<GetListResponseDto>> getList(){
        List<GetListResponseDto> response = productService.getList();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "상품 수정", description = "상품 ID로 대상을 지정하고, 상품명·가격·재고 수량·상태를 수정합니다.")
    @PatchMapping("/{id}")
    public ResponseEntity<UpdateResponseDto> update(
            @RequestBody UpdateRequestDto request,
            @PathVariable("id") Long id
    ){
        UpdateResponseDto response = productService.update(request, id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "상품 삭제", description = "상품 ID로 대상을 지정하여 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id") Long id
    ){
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
