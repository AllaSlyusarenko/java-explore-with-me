package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.dto.UserRequestDto;
import ru.practicum.ewm.user.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    UserResponseDto saveUser(UserRequestDto userRequestDto);

    List<UserResponseDto> findAllUsers(List<Long> ids, Integer from, Integer size);

    void deleteUserById(Long userId);
}