package com.camilagksantos.finance.unit.application.mapper;

import com.camilagksantos.finance.application.dto.response.UserResponse;
import com.camilagksantos.finance.application.mapper.UserMapperImpl;
import com.camilagksantos.finance.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @InjectMocks
    private UserMapperImpl mapper;

    @Test
    void shouldMapDomainToResponse() {
        User user = new User(1L, "Camila Santos", "camila.santos", "camila@email.com", "11999990001");

        UserResponse result = mapper.toResponse(user);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Camila Santos");
        assertThat(result.username()).isEqualTo("camila.santos");
        assertThat(result.email()).isEqualTo("camila@email.com");
        assertThat(result.phone()).isEqualTo("11999990001");
    }
}