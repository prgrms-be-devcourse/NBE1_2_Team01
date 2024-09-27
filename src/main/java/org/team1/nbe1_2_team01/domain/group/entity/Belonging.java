package org.team1.nbe1_2_team01.domain.group.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.team1.nbe1_2_team01.domain.board.entity.Board;
import org.team1.nbe1_2_team01.domain.calendar.entity.Calendar;
import org.team1.nbe1_2_team01.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "belonging")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Belonging {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean isOwner;

    private String course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToMany(mappedBy = "belonging")
    private List<Board> boards = new ArrayList<>();

    @Builder
    private Belonging(User user,
                      boolean isOwner,
                      String course) {
        this.user = user;
        this.course = course;
        this.isOwner = isOwner;
        if (user != null) user.addBelonging(this);
    }

    public static Belonging createBelongingOf(boolean isOwner, String course, User user) {
        return Belonging.builder()
                .isOwner(isOwner)
                .course(course)
                .user(user)
                .build();
    }

    public void assignTeam(Team team){
        this.team = team;
    }

    public void addBoards(Board board) {
        this.boards.add(board);
    }
}
