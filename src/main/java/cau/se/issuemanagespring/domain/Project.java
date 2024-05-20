package cau.se.issuemanagespring.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Project extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    private User projectOwner;

    @ManyToMany
    private List<User> PLs = new ArrayList<>();

    @ManyToMany
    private List<User> Devs = new ArrayList<>();

    @ManyToMany
    private List<User> Testers = new ArrayList<>();
}
