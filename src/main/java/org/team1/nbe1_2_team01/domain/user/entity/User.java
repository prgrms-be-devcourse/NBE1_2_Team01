package org.team1.nbe1_2_team01.domain.user.entity;

import jakarta.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    private String email;
    private String name;

    @Enumerated(value = EnumType.STRING)
    private Role role;

}
