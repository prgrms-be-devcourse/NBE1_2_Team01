package org.team1.nbe1_2_team01.domain.group.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team1.nbe1_2_team01.domain.group.entity.Belonging;
import org.team1.nbe1_2_team01.domain.group.repository.BelongingRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BelongingService {

    private final BelongingRepository belongingRepository;

    public List<Long> courseUserList(String course) {
        return belongingRepository.findDistinctUserIdsByCourse(course);
    }

}
