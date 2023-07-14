package ru.practicum.ewm.stats.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class Stats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_app")
    private String app;

    @Column(name = "uri")
    private String uri;

    @Column(name = "ip_user")
    private String ip;

    @Column(name = "created_date")
    private LocalDateTime created;

}
