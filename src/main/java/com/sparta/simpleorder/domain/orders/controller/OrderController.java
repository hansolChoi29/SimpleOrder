package com.sparta.simpleorder.domain.orders.controller;


import com.sparta.simpleorder.domain.orders.dto.request.CreateRequestDto;
import com.sparta.simpleorder.domain.orders.dto.response.CreateResponseDto;
import com.sparta.simpleorder.domain.orders.dto.response.GetListResponseDto;
import com.sparta.simpleorder.domain.orders.dto.response.GetOneResponseDto;
import com.sparta.simpleorder.domain.orders.dto.response.UpdateResponseDto;
import com.sparta.simpleorder.domain.orders.service.OrderService;
import com.sparta.simpleorder.domain.orders.dto.request.UpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    ) {
        CreateResponseDto response = orderService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetOneResponseDto> getOne(
            @PathVariable("id") Long id
    ) {
        GetOneResponseDto response = orderService.getOne(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<GetListResponseDto>> getList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GetListResponseDto> response = orderService.getList(pageable);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UpdateResponseDto> update(
            @RequestBody UpdateRequestDto request,
            @PathVariable("id") Long id
    ){
        UpdateResponseDto response = orderService.update(request, id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @PathVariable("id") Long id
    ){
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
