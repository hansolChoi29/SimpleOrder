package com.sparta.simpleorder.domain.orders.controller;


import com.sparta.simpleorder.domain.orders.dto.request.CreateRequestDto;
import com.sparta.simpleorder.domain.orders.dto.response.CreateResponseDto;
import com.sparta.simpleorder.domain.orders.dto.response.GetOneResponseDto;
import com.sparta.simpleorder.domain.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<CreateResponseDto> create(
            @RequestBody CreateRequestDto request
    ){
        CreateResponseDto response= orderService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetOneResponseDto> getOne(
            @PathVariable("id") Long id
    ){
        GetOneResponseDto response = orderService.getOne(id);
        return ResponseEntity.ok(response);
    }
}
