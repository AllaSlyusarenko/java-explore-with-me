package ru.practicum.ewm.comment.model;

public enum CommentStatus {
    PENDING, //в ожидании модерации, виден автору, админу
    CANCELED, // отозван самим пользователем, виден автору, админу
    PUBLISHED, // опубликован админом, виден всем, автору, админу
    REJECTED // отклонен админом, виден админу
}