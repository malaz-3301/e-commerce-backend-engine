package com.saea.bookecommerce.controller;

import com.saea.bookecommerce.dto.AuthResponse;
import com.saea.bookecommerce.model.User;
import com.saea.bookecommerce.model.UserRole;
import com.saea.bookecommerce.security.JwtService;
import com.saea.bookecommerce.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    private UserController userController;
    private User user;

    @BeforeEach
    void setUp() {
        userController = new UserController(userService, jwtService);

        user = new User();
        user.setId(1L);
        user.setName("Malaz Ahmad");
        user.setEmail("malaz@example.com");
        user.setPassword("123456");
        user.setRole(UserRole.CUSTOMER);
    }

    @Test
    void createUserReturnsToken() {
        when(userService.create(user)).thenReturn(user);
        when(jwtService.generateToken("malaz@example.com", "CUSTOMER")).thenReturn("token-value");

        AuthResponse response = userController.create(user);

        assertThat(response.getToken()).isEqualTo("token-value");
    }
}
