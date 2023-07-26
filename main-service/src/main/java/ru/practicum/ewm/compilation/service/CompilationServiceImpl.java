package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class CompilationServiceImpl implements CompilationService {
    final CompilationRepository compilationRepository;
    final EventRepository eventRepository;

    @Override
    public CompilationDto saveCompilation(NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getPinned() == null) {
            newCompilationDto.setPinned(false);
        }
        if (newCompilationDto.getTitle() == null || newCompilationDto.getTitle().isBlank()) {
            throw new ValidationException("Поле title является обязательным для заполнения");
        }
        List<Event> eventsEntities = new ArrayList<>();
        if (newCompilationDto.getEvents() != null) {
            eventsEntities.addAll(listLongToListEvent(newCompilationDto.getEvents()));
        }
        Compilation compilation = CompilationMapper.newCompilationDtoToCompilation(newCompilationDto, eventsEntities);
        Compilation compilationSave = compilationRepository.save(compilation);
        return CompilationMapper.compilationToCompilationDto(compilationSave);
    }

    @Override
    public void deleteCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Подборка не найдена"));
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilationById(Long compId, NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Подборка не найдена"));
        if (newCompilationDto.getEvents() != null) {
            compilation.setEvents(listLongToListEvent(newCompilationDto.getEvents()));
        }
        if (newCompilationDto.getPinned() != null) {
            compilation.setPinned(newCompilationDto.getPinned());
        }
        if (newCompilationDto.getTitle() != null) {
            compilation.setTitle(newCompilationDto.getTitle());
        }
        Compilation compilationSave = compilationRepository.save(compilation);
        return CompilationMapper.compilationToCompilationDto(compilationSave);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id"));
        List<Compilation> compilations = new ArrayList<>();
        if (pinned != null) {
            compilations.addAll(compilationRepository.findAllByPinned(pinned, pageable));
        } else {
            compilations.addAll(compilationRepository.findAll(pageable).getContent());
        }
        return CompilationMapper.compilationsToCompilationDtos(compilations);
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Подборка не найдена"));
        return CompilationMapper.compilationToCompilationDto(compilation);
    }

    private List<Event> listLongToListEvent(List<Long> ids) {
        return ids.stream().map(id -> eventRepository.findById(id).orElseThrow()).collect(Collectors.toList());
    }
}