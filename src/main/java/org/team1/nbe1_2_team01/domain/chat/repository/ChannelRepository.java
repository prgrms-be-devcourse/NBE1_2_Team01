package org.team1.nbe1_2_team01.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.team1.nbe1_2_team01.domain.chat.entity.Channel;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long>{

}
