package ru.practicum.ewm.comment.model;

import lombok.*;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "likes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @OneToOne
    @JoinColumn(name = "comment")
    private Comment comment;

    @NonNull
    @JoinColumn(name = "is_like")
    private Boolean isLike; // true - like, false - dislike

    @OneToOne
    @JoinColumn(name = "liker")
    private User liker;

    @JoinColumn(name = "created")
    private LocalDateTime created;
}