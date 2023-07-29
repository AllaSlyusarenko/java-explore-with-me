package ru.practicum.ewm.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.UserRequestDto;
import ru.practicum.ewm.user.dto.UserResponseDto;
import ru.practicum.ewm.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserAdminController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto saveUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        log.debug("Создание пользователя: {}", userRequestDto);
        return userService.saveUser(userRequestDto);
    }

    @GetMapping
    public List<UserResponseDto> findAllUsers(
            @RequestParam(value = "ids", required = false) List<Long> ids,
            @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        log.debug("Просмотр пользователей с id из {} ", ids);
        return userService.findAllUsers(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable(value = "userId") Long userId) {
        log.debug("Удаление пользователя с id {} ", userId);
        userService.deleteUserById(userId);
    }
}