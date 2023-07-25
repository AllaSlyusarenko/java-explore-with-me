package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.repository.CompilationRepository;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class CompilationServiceImpl implements CompilationService {
    CompilationRepository compilationRepository;

    @Override
    public CompilationDto saveCompilation(NewCompilationDto newCompilationDto) {
        return null;
    }
}
