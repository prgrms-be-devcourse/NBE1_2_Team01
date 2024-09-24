package org.team1.nbe1_2_team01.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String username;

    private String password;

    @Column(length = 50)
    private String email;

    @Column(length = 10)
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;



}
