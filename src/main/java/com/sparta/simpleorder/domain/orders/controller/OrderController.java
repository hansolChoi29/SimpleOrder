package com.sparta.simpleorder.domain.orders.controller;


import com.sparta.simpleorder.domain.orders.dto.request.CreateRequestDto;
import com.sparta.simpleorder.domain.orders.dto.response.CreateResponseDto;
import com.sparta.simpleorder.domain.orders.dto.response.GetListResponseDto;
import com.sparta.simpleorder.domain.orders.dto.response.GetOneResponseDto;
import com.sparta.simpleorder.domain.orders.dto.response.UpdateResponseDto;
import com.sparta.simpleorder.domain.orders.service.OrderService;
import com.sparta.simpleorder.domain.orders.dto.request.UpdateRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Orders", description = "주문 등록·조회·수정·삭제 API")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "주문 생성", description = "상품 ID와 주문 수량을 입력하여 주문을 생성합니다.")
    @PostMapping
    public ResponseEntity<CreateResponseDto> create(
            @RequestBody CreateRequestDto request
    ) {
        CreateResponseDto response = orderService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "주문 단건조회", description = "주문 ID로 특정 주문의 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<GetOneResponseDto> getOne(
            @PathVariable("id") Long id
    ) {
        GetOneResponseDto response = orderService.getOne(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "주문 전체조회", description = "page와 size를 입력하여 페이지 기반으로 주문 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<GetListResponseDto>> getList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GetListResponseDto> response = orderService.getList(pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "주문 수정", description = "주문 ID로 대상을 지정하고, 주문 수량·상태를 수정합니다.")
    @PatchMapping("/{id}")
    public ResponseEntity<UpdateResponseDto> update(
            @RequestBody UpdateRequestDto request,
            @PathVariable("id") Long id
    ){
        UpdateResponseDto response = orderService.update(request, id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "주문 삭제", description = "주문 ID로 대상을 지정하여 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @PathVariable("id") Long id
    ){
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
