package ru.practicum.ewm.request.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.dto.RequestStatus;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.utility.Constants;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "created")
    @JsonFormat(pattern = Constants.DATE_PATTERN_FULL)
    private LocalDateTime created;

    @OneToOne
    @JoinColumn(name = "event")
    private Event event;

    @OneToOne
    @JoinColumn(name = "requester")
    private User requester;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "status")
    private RequestStatus status;
}