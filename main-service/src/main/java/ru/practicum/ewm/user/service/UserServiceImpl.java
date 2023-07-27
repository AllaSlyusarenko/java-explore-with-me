package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.dto.UserRequestDto;
import ru.practicum.ewm.user.dto.UserResponseDto;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserResponseDto saveUser(UserRequestDto userRequestDto) {
        User user = userRepository.save(UserMapper.requestDtoToUser(userRequestDto));
        return UserMapper.userToUserResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> findAllUsers(List<Long> ids, Integer from, Integer size) {
        List<User> result;
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));
        if (ids == null || ids.isEmpty()) {
            result = userRepository.findAll(pageable).toList();
        } else {
            result = userRepository.findAllByIdIn(ids, pageable);
        }
        return UserMapper.usersToUserResponseDtos(result);
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        userRepository.deleteById(userId);
    }
}