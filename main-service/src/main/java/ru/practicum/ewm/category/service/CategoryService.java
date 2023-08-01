package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.dto.CategoryRequestDto;
import ru.practicum.ewm.category.dto.CategoryResponseDto;

import java.util.List;

public interface CategoryService {
    CategoryResponseDto saveCategory(CategoryRequestDto categoryRequestDto);

    void deleteCategoryById(Long catId);

    CategoryResponseDto updateCategoryById(Long catId, CategoryRequestDto categoryRequestDto);

    List<CategoryResponseDto> findAllCategories(Integer from, Integer size);

    CategoryResponseDto findCategoryById(Long catId);
}