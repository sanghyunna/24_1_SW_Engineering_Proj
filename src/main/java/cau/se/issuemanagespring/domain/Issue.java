package cau.se.issuemanagespring.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private LocalDateTime reportDate;

    private LocalDateTime updateDate;

    private LocalDateTime dueDate;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    private User reporter;

    @ManyToMany
    private List<User> assignee = new ArrayList<>();

    @ManyToOne
    private User fixer;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @OneToMany
    private List<Comment> comments = new ArrayList<>();
}

