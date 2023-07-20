package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.dto.CategoryRequestDto;
import ru.practicum.ewm.category.dto.CategoryResponseDto;

import java.util.List;

public interface CategoryService {
    public CategoryResponseDto saveCategory(CategoryRequestDto categoryRequestDto);

    public void deleteCategoryById(Long catId);

    public CategoryResponseDto updateCategoryById(Long catId, CategoryRequestDto categoryRequestDto);

    public List<CategoryResponseDto> findAllCategories(Integer from, Integer size);

    public CategoryResponseDto findCategoryById(Long catId);
}
