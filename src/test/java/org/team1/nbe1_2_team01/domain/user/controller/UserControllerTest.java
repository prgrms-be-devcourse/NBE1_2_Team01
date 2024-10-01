package org.team1.nbe1_2_team01.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.team1.nbe1_2_team01.domain.user.controller.request.UserSignUpRequest;
import org.team1.nbe1_2_team01.domain.user.service.UserService;
import org.team1.nbe1_2_team01.domain.user.service.response.UserIdResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void 회원가입_성공() throws Exception {
        // 요청과 응답 데이터 설정
        UserSignUpRequest request = new UserSignUpRequest(
                "userA",
                "1234abcd",
                "user@gmail.com",
                "김철수");
        UserIdResponse response = new UserIdResponse(1L);

        //mocking 된 객체의 특정 메서드 호출시 return_value를 반환하도록 설정
        when(userService.signUp(any(UserSignUpRequest.class))).thenReturn(response);

        // 응답 검증
        mockMvc.perform(post("/api/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"))
                .andExpect(jsonPath("$.result.id").value(1L));
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
    void 회원가입_실패_아이디는_영문과숫자여야한다() throws Exception{
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
    void 회원가입_실패_비밀번호는_8자리이상이어야한다() throws Exception{
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
    void 회원가입_실패_비밀번호는_영문을_포함해야한다() throws Exception{
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
    void 회원가입_실패_비밀번호는_숫자를_포함해야한다() throws Exception{
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
    void 회원가입_실패_이메일은_이메일형식이어야한다() throws Exception{
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
    void 회원가입_실패_이름은_2글자이상이어야한다() throws Exception{
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
}