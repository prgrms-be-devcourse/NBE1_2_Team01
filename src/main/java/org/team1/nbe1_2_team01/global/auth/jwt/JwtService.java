//package org.team1.nbe1_2_team01.global.auth.jwt;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;
//
//import java.util.Date;
//import java.util.Optional;
//
// 희수 : 에러때메 잠시 주석
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class JwtService {
//
//    @Value("${jwt.secretKey}")
//    private String secretKey;
//
//    @Value("${jwt.access.expiration}")
//    private Long accessTokenExpirationPeriod;
//
//    @Value("${jwt.refresh.expiration}")
//    private Long refreshTokenExpirationPeriod;
//
//    @Value("${jwt.access.header}")
//    private String accessHeader;
//
//    @Value("${jwt.refresh.header}")
//    private String refreshHeader;
//
//    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
//    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
//    private static final String USERNAME_CLAIM = "username";
//    private static final String BEARER = "Bearer ";
//
//    private final UserRepository userRepository;
//
//    /**
//     * AccessToken 생성 메소드
//     */
//
//    public String creationAccessToken(String username) {
//        Date now = new Date();
//        Claims claims = Jwts.claims();
//        claims.put(USERNAME_CLAIM, username);
//        return Jwts.builder()
//                .setSubject(ACCESS_TOKEN_SUBJECT)
//                .setExpiration(new Date(now.getTime() + accessTokenExpirationPeriod))
//                .setClaims(claims)
//                .signWith(SignatureAlgorithm.HS256, secretKey)
//                .compact();
//    }
//
//    /**
//     * RefreshToken 생성
//     * RefreshToken은 Claim에 아무것도 넣지 않음
//     */
//    public String createRefreshToken() {
//        Date now = new Date();
//        return Jwts.builder()
//                .setSubject(REFRESH_TOKEN_SUBJECT)
//                .setExpiration(new Date(now.getTime() + refreshTokenExpirationPeriod))
//                .signWith(SignatureAlgorithm.HS256, secretKey)
//                .compact();
//    }
//
//    /**
//     * AccessToken 헤더에 실어서 보내기
//     */
//    public void sendAccessToken(HttpServletResponse response, String accessToken) {
//        response.setStatus(HttpServletResponse.SC_OK);
//
//        response.setHeader(accessHeader, accessToken);
//        log.info("재발급된 Access Token : {}", accessToken);
//    }
//
//    /**
//     * AccessToken + RefreshToken 헤더에 실어서 보내기
//     */
//    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
//        response.setStatus(HttpServletResponse.SC_OK);
//
//        setAccessTokenHeader(response, accessToken);
//        setRefreshTokenHeader(response, refreshToken);
//        log.info("Access Token, Refresh Token 헤더 설정 완료");
//    }
//
//
//    /**
//     * 헤더에서 Token 추출
//     * 토큰형식 : Bearer를 제외하고 순수 토큰만 가져오기 위해서
//     * 헤더를 가져온 후 "Bearer"를 삭제 후 반환
//     */
//    public Optional<String> extractToken(HttpServletRequest request) {
//        return Optional.ofNullable(request.getHeader(accessHeader))
//                .filter(refreshToken -> refreshToken.startsWith(BEARER))
//                .map(refreshToken -> refreshToken.replace(BEARER, ""));
//    }
//
//    /**
//     * AccessToken에서 username 추출
//     * 추출 전에 JWT.require()로 검증기 생성
//     * verify로 AceessToken 검증 후
//     * 유효하다면 getClaim()으로 username 추출
//     * 유효하지 않다면 빈 Optional 객체 반환
//     */
//    public Optional<String> extractUsername(String accessToken) {
//        try {
//            Claims claims = Jwts.parser()
//                    .setSigningKey(secretKey)
//                    .parseClaimsJws(accessToken)
//                    .getBody();
//
//            return Optional.ofNullable(claims.get(USERNAME_CLAIM, String.class)); // username 클레임 가져오기
//        } catch (Exception e) {
//            log.error("액세스 토큰이 유효하지 않습니다.");
//            return Optional.empty();
//        }
//    }
//
//    /**
//     * AccessToken 헤더 설정
//     */
//    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
//        response.setHeader(accessHeader, accessToken);
//    }
//
//    /**
//     * RefreshToken 헤더 설정
//     */
//    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
//        response.setHeader(refreshHeader, refreshToken);
//    }
//
//    /**
//     * RefreshToken DB 저장(업데이트)
//     */
////    public void updateRefreshToken(String email, String refreshToken) {
////        userRepository.findByEmail(email)
////                .ifPresentOrElse(
////                        user -> user.updateRefreshToken(refreshToken),
////                        () -> new Exception("일치하는 회원이 없습니다.")
////                );
////    }
//
//    /**
//     * token을 파싱해서 성공 하면 true 반환
//     *  실패하면 false 반환
//     */
//
//    public boolean isTokenValid(String token) {
//        try {
//            Jwts.parser()
//                    .setSigningKey(secretKey)
//                    .parseClaimsJws(token);
//            return true;
//        } catch (Exception e) {
//            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
//            return false;
//        }
//    }
//
//}
