package ru.practicum.ewm.compilation.service;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;

public interface CompilationService {
    CompilationDto saveCompilation(NewCompilationDto newCompilationDto);
}
