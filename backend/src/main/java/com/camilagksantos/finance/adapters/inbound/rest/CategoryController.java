package com.camilagksantos.finance.adapters.inbound.rest;

import com.camilagksantos.finance.application.dto.request.CategoryRequest;
import com.camilagksantos.finance.application.dto.response.CategoryResponse;
import com.camilagksantos.finance.application.mapper.CategoryMapper;
import com.camilagksantos.finance.domain.ports.input.CategoryUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryUseCase categoryUseCase;
    private final CategoryMapper categoryMapper;

    public CategoryController(CategoryUseCase categoryUseCase, CategoryMapper categoryMapper) {
        this.categoryUseCase = categoryUseCase;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                categoryMapper.toResponse(categoryUseCase.getById(id))
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CategoryResponse>> getAllByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(
                categoryUseCase.getAllByUserId(userId)
                        .stream()
                        .map(categoryMapper::toResponse)
                        .toList()
        );
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryMapper.toResponse(
                        categoryUseCase.create(categoryMapper.toDomain(request))
                ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable UUID id,
                                                   @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(
                categoryMapper.toResponse(
                        categoryUseCase.update(id, categoryMapper.toDomain(request))
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        categoryUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
