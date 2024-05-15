package cau.se.issuemanagespring.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    private User commentOwner;
}
