package ru.practicum.ewm.event.dto;

import lombok.*;
import ru.practicum.ewm.category.dto.CategoryResponseDto;
import ru.practicum.ewm.user.dto.UserShortDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EventShortDto { //Respose short
    private String annotation;
    private CategoryResponseDto category;
    private Integer confirmedRequests; //Количество одобренных заявок на участие в данном событии
    private String eventDate; //Дата и время, на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
    private Long id;
    private UserShortDto initiator;
    private Boolean paid; //надо ли платить
    private String title; //Заголовок
    private Integer views; //Количество просмотрев события
}

//        List[
//                OrderedMap{
//                "annotation":"Эксклюзивность нашего шоу гарантирует привлечение максимальной зрительской аудитории",
//                "category":OrderedMap{"id":1,"name":"Концерты"},
//                "confirmedRequests":5,
//                "eventDate":"2024-03-10 14:30:00",
//                "id":1,
//                "initiator":
//                OrderedMap{"id":3,"name":"Фёдоров Матвей"},
//                "paid":true,
//                "title":"Знаменитое шоу 'Летающая кукуруза'",
//                "views":999},
//                OrderedMap{
//                "annotation":"За почти три десятилетия группа 'Java Core' закрепились на сцене как группа, объединяющая поколения.",
//                "category":OrderedMap{"id":1,"name":"Концерты"},
//                "confirmedRequests":555,
//                "eventDate":"2025-09-13 21:00:00",
//                "id":1,
//                "initiator":OrderedMap{"id":3,"name":"Паша Петров"},
//                "paid":true,
//                "title":"Концерт рок-группы 'Java Core'",
//                "views":991}]