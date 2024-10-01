package org.team1.nbe1_2_team01.global.auth.login.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.team1.nbe1_2_team01.global.auth.jwt.service.JwtService;
import org.team1.nbe1_2_team01.global.auth.redis.repository.RefreshTokenRepository;
import org.team1.nbe1_2_team01.global.auth.redis.token.RefreshToken;

import java.io.IOException;

@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = extractUsername(authentication);
        String accessToken = jwtService.createAccessToken(username);
        String refreshToken = jwtService.createRefreshToken();
        RefreshToken redis = RefreshToken.builder()
                .username(username)
                .token(refreshToken)
                .build();
        refreshTokenRepository.save(redis);
        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);

    }

    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
