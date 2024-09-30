package org.team1.nbe1_2_team01.global.auth.redis.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 *  value는 redis key 값으로 사용됨
 *  redis 저장소의 key : value 형식으로  {value} : {@Id 값} 저장
 *  유효 시간 : 60 * 60 * 24 * 14 -> 2주
 */
@Getter
@AllArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 24 * 14)
public class RefreshToken {

    @Id
    private String refreshToken;
    private String username;

}
