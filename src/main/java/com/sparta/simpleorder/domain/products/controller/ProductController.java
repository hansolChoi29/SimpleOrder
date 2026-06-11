package com.sparta.simpleorder.domain.products.controller;

import com.sparta.simpleorder.domain.products.dto.request.CreateRequestDto;
import com.sparta.simpleorder.domain.products.dto.request.UpdateRequest;
import com.sparta.simpleorder.domain.products.dto.response.CreateResponseDto;
import com.sparta.simpleorder.domain.products.dto.response.GetListResponse;
import com.sparta.simpleorder.domain.products.dto.response.GetOneResponse;
import com.sparta.simpleorder.domain.products.dto.response.UpdateResponse;
import com.sparta.simpleorder.domain.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<CreateResponseDto> create(
            @RequestBody CreateRequestDto request
    ) {
        CreateResponseDto response = productService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetOneResponse> getone(
            @PathVariable("id") Long id
    ) {
        GetOneResponse response = productService.getOne(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<GetListResponse>> getList(){
        List<GetListResponse> response = productService.getList();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UpdateResponse> update(
            @RequestBody UpdateRequest request,
            @PathVariable("id") Long id
    ){
        UpdateResponse response = productService.update(request, id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id") Long id
    ){
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
