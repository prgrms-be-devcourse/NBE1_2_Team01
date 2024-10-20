package org.team1.nbe1_2_team01.global.auth.jwt.filter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_2_team01.global.auth.jwt.service.JwtService;
import org.team1.nbe1_2_team01.global.auth.redis.token.RefreshToken;
import org.team1.nbe1_2_team01.global.auth.redis.repository.RefreshTokenRepository;

import java.io.IOException;

/**
 * Jwt 인증 필터
 * "/login 이외의 URI 요청을 처리하는 필터
 *  RTR 방식으로 동작
 * 1. RefreshToken이 없고, AccessToken이 유효한 경우 -> 인증 성공
 * 2. RefreshToken이 없고, AccessToken이 없거나 유효X 인 경우 -> 인증 실패
 * 3. RefreshToken이 있는 경우 -> RefreshToken과 비교하여 일치하면 AccessToken 재발급 RefreshToken 재발급
 */
@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {
    private static final String NO_CHECK_URL = "/api/login";

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 로그인 요청 들어오면 다음 필터 호출
        if (request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        if (refreshToken != null) {
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            return;
        }

        checkAccessTokenAndAuthentication(request, response, filterChain);
    }

    public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(token -> {
                    String username = token.getUsername();
                    User user = userRepository.findByUsername(username)
                            .orElseThrow(() -> new UsernameNotFoundException("해당 사용자가 존재하지 않습니다"));
                    String reIssuedRefreshToken = jwtService.createRefreshToken(username);
                    jwtService.sendAccessAndRefreshToken(response, jwtService.createAccessToken(username), reIssuedRefreshToken);
                });
    }

    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                  FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtService.extractAccessToken(request)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("AccessToken이 없습니다."));

        try {
            jwtService.isTokenValid(accessToken);
            jwtService.extractUsername(accessToken)
                    .flatMap(userRepository::findByUsername)
                    .ifPresent(this::saveAuthentication);
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("AccessToken이 만료되었습니다. 새로운 AccessToken을 요청하세요.");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("유효하지 않은 AccessToken입니다.");
        }
    }

    public void saveAuthentication(User user) {
        String password = user.getPassword();

        UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(password)
                .roles(user.getRole().name())
                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                        authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
