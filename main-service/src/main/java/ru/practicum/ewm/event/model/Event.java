package ru.practicum.ewm.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.dto.EventState;
import ru.practicum.ewm.request.dto.ParticipationStatus;
import ru.practicum.ewm.request.model.Participation;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.utility.Constants;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "annotation")
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

//    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
//    @JsonManagedReference
//    private Collection<Participation> requests;

    @JoinColumn(name = "confirmed_requests")
    private Integer confirmedRequests; //Количество одобренных заявок на участие в данном событии

    @Column(name = "created_on")
    @JsonFormat(pattern = Constants.DATE_PATTERN_FULL)
    private LocalDateTime createdOn; //Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss")

    @JoinColumn(name = "description")
    private String description;

    @Column(name = "event_date")
    @JsonFormat(pattern = Constants.DATE_PATTERN_FULL)
    private LocalDateTime eventDate; //Дата и время, на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @OneToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @JoinColumn(name = "paid")
    private Boolean paid; //надо ли платить

    @JoinColumn(name = "participant_limit")
    private Integer participantLimit; //Ограничение на количество участников. Значение 0 - означает отсутствие ограничения

    @JoinColumn(name = "published_on")
    @JsonFormat(pattern = Constants.DATE_PATTERN_FULL)
    private LocalDateTime publishedOn; //Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss")

    @JoinColumn(name = "request_moderation")
    private Boolean requestModeration; //Нужна ли пре-модерация заявок на участие

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "state")
    private EventState state; //enum

    @JoinColumn(name = "title")
    private String title; //Заголовок

    private Integer views;

//    public Collection<Participation> getConfirmedRequests() {
//        if (this.getRequests() == null) {
//            return new ArrayList<>();
//        }
//        return this.getRequests().stream()
//                .filter((request) -> request.getStatus() == ParticipationStatus.CONFIRMED)
//                .collect(Collectors.toUnmodifiableList());
//    }
}