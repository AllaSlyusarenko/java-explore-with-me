package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryResponseDto;
import ru.practicum.ewm.category.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryPublicController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryResponseDto> findAllCategories(
            @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        log.debug("Просмотр категорий");
        return categoryService.findAllCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryResponseDto findCategoryById(@PathVariable(value = "catId") Long catId) {
        log.debug("Просмотр категорий c id: {}", catId);
        return categoryService.findCategoryById(catId);
    }
}