package org.team1.nbe1_2_team01.domain.group.entity;

import jakarta.persistence.*;
import org.team1.nbe1_2_team01.domain.user.entity.User;

@Entity
public class Belonging {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean isOwner;

    private String course;

//    private User user;
//
//    private Team team;

}
