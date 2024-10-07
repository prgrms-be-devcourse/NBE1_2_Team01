package org.team1.nbe1_2_team01.domain.board.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "board_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "belonging_id")
    private Belonging belonging;

    @OneToMany(mappedBy = "category")
    private List<TeamBoard> boards = new ArrayList<>();


    @Builder
    private Category(Belonging belonging,
                     String name) {
        this.belonging = belonging;
        this.name = name;
    }

    public void addBoards(TeamBoard board) {
        this.boards.add(board);
    }
}
