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
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @ManyToOne
    private User projectOwner;

    @ManyToMany
    private List<User> PLs = new ArrayList<>();

    @ManyToMany
    private List<User> Devs = new ArrayList<>();

    @ManyToMany
    private List<User> Testers = new ArrayList<>();
}
