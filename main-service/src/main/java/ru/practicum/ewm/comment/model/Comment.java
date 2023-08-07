package ru.practicum.ewm.comment.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.utility.Constants;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "event")
    private Event event;

    @OneToOne
    @JoinColumn(name = "author")
    private User author;

    @JoinColumn(name = "created")
    @JsonFormat(pattern = Constants.DATE_PATTERN_FULL)
    private LocalDateTime created;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "status")
    private CommentStatus status;

    @JoinColumn(name = "edit_time")
    @JsonFormat(pattern = Constants.DATE_PATTERN_FULL)
    private LocalDateTime editTime;
}