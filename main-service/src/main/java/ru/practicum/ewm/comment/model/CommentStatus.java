package ru.practicum.ewm.comment.model;

public enum CommentStatus {
    PENDING, //в ожидании
    CANCELED, // отозван самим пользователем
    PUBLISHED, // опубликован админом
    REJECTED // отклонен админом
}