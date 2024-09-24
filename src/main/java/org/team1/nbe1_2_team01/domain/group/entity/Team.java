package org.team1.nbe1_2_team01.domain.group.entity;

import jakarta.persistence.*;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private TeamType teamType;

    private String name;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean creationWaiting;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean deletionWaiting;
}
