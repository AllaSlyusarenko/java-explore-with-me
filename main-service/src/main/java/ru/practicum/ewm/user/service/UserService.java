package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.dto.UserRequestDto;
import ru.practicum.ewm.user.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    public UserResponseDto saveUser(UserRequestDto userRequestDto);

    public List<UserResponseDto> findAllUsers(List<Long> ids, Integer from, Integer size);

    public void deleteUserById(Long userId);
}
