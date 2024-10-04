package org.team1.nbe1_2_team01.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_2_team01.IntegrationTestSupport;
import org.team1.nbe1_2_team01.domain.user.controller.request.UserSignUpRequest;
import org.team1.nbe1_2_team01.domain.user.controller.request.UserUpdateRequest;
import org.team1.nbe1_2_team01.domain.user.entity.Role;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_2_team01.domain.user.service.UserService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@Transactional
class UserControllerTest extends IntegrationTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;


    @Test
    void 회원가입_성공() throws Exception {
        // 요청과 응답 데이터 설정
        UserSignUpRequest request = new UserSignUpRequest(
                "userA",
                "1234abcd",
                "user@gmail.com",
                "김철수");
        // 응답 검증
        MvcResult result = mockMvc.perform(post("/api/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"))
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        Long resultId = JsonPath.parse(jsonResponse).read("$.result.id", Long.class);

        User savedUser = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 없음"));
        assertThat(savedUser.getId()).isEqualTo(resultId);
    }

    @Test
    void 회원가입_실패_아이디는_5이상_20이하여야한다() throws Exception {
        UserSignUpRequest request = new UserSignUpRequest(
                "user",
                "1234abcd",
                "user@gmail.com"
                , "김철수"
        );
        mockMvc.perform(post("/api/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message.username").value("아이디는 5자 이상, 20자 이하여야 합니다."));

    }

    @Test
    void 회원가입_실패_아이디는_영문과숫자여야한다() throws Exception {
        UserSignUpRequest request = new UserSignUpRequest(
                "가나다라마바",
                "1234abcd",
                "user@gmail.com"
                , "김철수"
        );
        mockMvc.perform(post("/api/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message.username").value("아이디는 영문과 숫자만 사용할 수 있습니다."));
    }

    @Test
    void 회원가입_실패_비밀번호는_8자리이상이어야한다() throws Exception {
        UserSignUpRequest request = new UserSignUpRequest(
                "userA",
                "1234abc",
                "user@gmail.com"
                , "김철수"
        );
        mockMvc.perform(post("/api/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message.password").value("비밀번호는 8자 이상이어야 합니다."));
    }

    @Test
    void 회원가입_실패_비밀번호는_영문을_포함해야한다() throws Exception {
        UserSignUpRequest request = new UserSignUpRequest(
                "userA",
                "12341234",
                "user@gmail.com"
                , "김철수"
        );
        mockMvc.perform(post("/api/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message.password").value("비밀번호는 영문과 숫자를 포함해야 합니다."));
    }

    @Test
    void 회원가입_실패_비밀번호는_숫자를_포함해야한다() throws Exception {
        UserSignUpRequest request = new UserSignUpRequest(
                "userA",
                "abcdabcd",
                "user@gmail.com"
                , "김철수"
        );
        mockMvc.perform(post("/api/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message.password").value("비밀번호는 영문과 숫자를 포함해야 합니다."));
    }

    @Test
    void 회원가입_실패_이메일은_이메일형식이어야한다() throws Exception {
        UserSignUpRequest request = new UserSignUpRequest(
                "userA",
                "1234abcd",
                "usergmail.com"
                , "김철수"
        );
        mockMvc.perform(post("/api/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message.email").value("유효하지 않은 이메일 형식입니다."));
    }

    @Test
    void 회원가입_실패_이름은_2글자이상이어야한다() throws Exception {
        UserSignUpRequest request = new UserSignUpRequest(
                "userA",
                "1234abcd",
                "user@gmail.com"
                , "김"
        );
        mockMvc.perform(post("/api/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message.name").value("이름은 2자 이상이어야 합니다."));
    }

    @Test
    @WithMockUser(username = "userA", roles = {"USER"})
    void 회원수정_성공() throws Exception {

        //가짜 사용자를 쓰더라도 DB에 사용자가 없으면 현재 로그인된 사용자를 찾는 과정에서 에러가 발생하기때문에 DB에 넣어준다
        User user = User.builder()
                .username("userA")
                .password("1234abcd")
                .email("user@gmail.com")
                .name("김철수")
                .role(Role.USER)
                .build();

        userRepository.save(user);
        UserUpdateRequest request = new UserUpdateRequest(
                "userB",
                "abcd1234"
        );

        mockMvc.perform(patch("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"))
                .andExpect(jsonPath("$.result.id").value(user.getId()));

        User updateuser = userRepository.findByUsername("userA")
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 없음"));

        assertThat(updateuser.getName()).isEqualTo(request.name());
        assertThat(passwordEncoder.matches(request.password(), updateuser.getPassword())).isTrue();
    }

    @Test
    @WithMockUser(username = "userA")
    void 회원정보조회_성공() throws Exception {

        User user = User.builder()
                .username("userA")
                .password("1234abcd")
                .email("user@gmail.com")
                .name("김철수")
                .role(Role.USER)
                .build();

        User saveduser = userRepository.save(user);

        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"))
                .andExpect(jsonPath("$.result.id").value(saveduser.getId()))
                .andExpect(jsonPath("$.result.username").value(saveduser.getUsername()))
                .andExpect(jsonPath("$.result.email").value(saveduser.getEmail()))
                .andExpect(jsonPath("$.result.name").value(saveduser.getName()));
    }

    @Test
    @WithMockUser(username = "userA", roles = {"USER"})
    void 전체사용자조회_사용자권한_접근실패() throws Exception {
        mockMvc.perform(get("/api/user/admin/all"))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void 전체사용자조회_사용자권한_접근성공() throws Exception {
        mockMvc.perform(get("/api/user/admin/all"))
                .andExpect(status().isOk());
    }
}