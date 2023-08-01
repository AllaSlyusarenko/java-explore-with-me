package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.dto.CategoryRequestDto;
import ru.practicum.ewm.category.dto.CategoryResponseDto;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryResponseDto saveCategory(CategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository.save(CategoryMapper.categoryRequestDtoToCategory(categoryRequestDto));
        return CategoryMapper.categoryToCategoryResponseDto(category);
    }

    @Override
    public void deleteCategoryById(Long catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Категория с id " + catId + " не найдена"));
        List<Event> events = eventRepository.findAllByCategory_Id(catId);
        if (events.isEmpty()) {
            categoryRepository.deleteById(catId);
        } else {
            throw new ConflictException("Существуют события, связанные с категорией");
        }
    }

    @Override
    public CategoryResponseDto updateCategoryById(Long catId, CategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Категория с id " + catId + " не найдена"));
        category.setName(categoryRequestDto.getName());
        Category categorySave = categoryRepository.save(category);
        return CategoryMapper.categoryToCategoryResponseDto(categorySave);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> findAllCategories(Integer from, Integer size) {
        List<Category> result;
        Pageable pageable = PageRequest.of(from / size, size);
        result = categoryRepository.findAll(pageable).toList();
        return CategoryMapper.categoriesToCategoryResponseDtos(result);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDto findCategoryById(Long catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Категория с id " + catId + " не найдена"));
        return CategoryMapper.categoryToCategoryResponseDto(category);
    }
}