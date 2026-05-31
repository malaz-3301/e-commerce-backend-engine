package com.saea.bookecommerce.service;

import com.saea.bookecommerce.exception.ResourceNotFoundException;
import com.saea.bookecommerce.model.User;
import com.saea.bookecommerce.model.UserRole;
import com.saea.bookecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Malaz Ahmad");
        user.setEmail("malaz@example.com");
        user.setPassword("123456");
        user.setRole(UserRole.CUSTOMER);
    }

    @Test
    void findAllReturnsUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> users = userService.findAll();

        assertThat(users).hasSize(1);
        assertThat(users.getFirst().getEmail()).isEqualTo("malaz@example.com");
    }

    @Test
    void findByIdReturnsUserWhenExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getRole()).isEqualTo(UserRole.CUSTOMER);
    }

    @Test
    void findByIdThrowsWhenUserDoesNotExist() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void createUser() {
        when(userRepository.save(user)).thenReturn(user);

        User saved = userService.create(user);

        assertThat(saved.getEmail()).isEqualTo("malaz@example.com");
        verify(userRepository).save(user);
    }

    @Test
    void updateUser() {
        User request = new User();
        request.setName("Updated Name");
        request.setEmail("updated@example.com");
        request.setPassword("new-password");
        request.setRole(UserRole.ADMIN);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User updated = userService.update(1L, request);

        assertThat(updated.getName()).isEqualTo("Updated Name");
        assertThat(updated.getEmail()).isEqualTo("updated@example.com");
        assertThat(updated.getRole()).isEqualTo(UserRole.ADMIN);
        verify(userRepository).save(user);
    }

    @Test
    void deleteUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.delete(1L);

        verify(userRepository).delete(user);
    }
}
