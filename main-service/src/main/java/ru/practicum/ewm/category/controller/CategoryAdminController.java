package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryRequestDto;
import ru.practicum.ewm.category.dto.CategoryResponseDto;
import ru.practicum.ewm.category.service.CategoryService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/admin/categories", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto saveCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        log.debug("Создание категории: {}", categoryRequestDto);
        return categoryService.saveCategory(categoryRequestDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryById(@PathVariable(value = "catId") Long catId) {
        log.debug("Удаление категории с id: {}", catId);
        categoryService.deleteCategoryById(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryResponseDto updateCategoryById(@PathVariable(value = "catId") Long catId,
                                                  @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        log.debug("Изменение категории с id: {}", catId);
        return categoryService.updateCategoryById(catId, categoryRequestDto);
    }
}