package com.camilagksantos.finance.unit.domain.service;

import com.camilagksantos.finance.domain.exception.ResourceNotFoundException;
import com.camilagksantos.finance.domain.model.User;
import com.camilagksantos.finance.domain.ports.output.UserOutputPort;
import com.camilagksantos.finance.domain.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserOutputPort userOutputPort;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Camila Santos", "camila.santos", "camila@email.com", "11999990001");
    }

    @Test
    void shouldGetUserById() {
        when(userOutputPort.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getById(1L);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Camila Santos");
        verify(userOutputPort, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userOutputPort.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getById(1L))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(userOutputPort, times(1)).findById(1L);
    }

    @Test
    void shouldGetAllUsers() {
        when(userOutputPort.findAll()).thenReturn(List.of(user));

        List<User> result = userService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().username()).isEqualTo("camila.santos");
        verify(userOutputPort, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoUsers() {
        when(userOutputPort.findAll()).thenReturn(List.of());

        List<User> result = userService.getAll();

        assertThat(result).isEmpty();
        verify(userOutputPort, times(1)).findAll();
    }
}