package com.example.mimir.entities;

import com.example.mimir.dto.Task;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "user_history")
public class UserHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, name = "id")
    private UUID id;

    @JoinColumn( nullable = false, name = "user_id" )
    @ManyToOne(fetch = FetchType.LAZY)
    private User userId;

    @Column (nullable = false, name = "event_date", columnDefinition = "TIMESTAMP")
    private Date eventDate;

    @Column (nullable = false, name = "mistakes_numbers")
    private Integer mistakesNumbers;

    @Column (nullable = false, name = "successes_numbers")
    private Integer successesNumbers;

    @Column (nullable = false, name = "task_payload", columnDefinition = "jsonb")
    @Convert(converter = Task.TaskJsonConverter.class)
    private Task taskPayload;
}
