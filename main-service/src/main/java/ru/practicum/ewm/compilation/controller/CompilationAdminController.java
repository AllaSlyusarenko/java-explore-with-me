package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.service.CompilationService;

import javax.validation.Valid;


@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/admin/compilations", produces = MediaType.APPLICATION_JSON_VALUE)
public class CompilationAdminController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto saveCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto){
        return compilationService.saveCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilationById(@PathVariable(name = "compId") Long compId){
        compilationService.deleteCompilationById(compId);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto updateCompilationById(@PathVariable(name = "compId") Long compId,
                                                @Valid @RequestBody NewCompilationDto newCompilationDto){
        return compilationService.updateCompilationById(compId, newCompilationDto);
    }
}