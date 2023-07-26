package ru.practicum.ewm.compilation.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {
    public Compilation newCompilationDtoToCompilation(NewCompilationDto newCompilationDto, List<Event> eventsEntities) {
        Compilation compilation = new Compilation();
        compilation.setEvents(eventsEntities);
        compilation.setPinned(newCompilationDto.getPinned());
        compilation.setTitle(newCompilationDto.getTitle());
        return compilation;
    }

    public CompilationDto compilationToCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setEvents(EventMapper.eventsToEventShortDto(compilation.getEvents()));
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());
        return compilationDto;
    }

    public List<CompilationDto> compilationsToCompilationDtos(List<Compilation> compilations) {
        return compilations.stream().map(x -> compilationToCompilationDto(x)).collect(Collectors.toList());
    }
}