package com.camilagksantos.finance.adapters.outbound.external.mock;

import com.camilagksantos.finance.domain.model.User;
import com.camilagksantos.finance.domain.ports.output.UserOutputPort;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Profile({"dev", "test"})
public class H2UserAdapter implements UserOutputPort {

    private static final List<User> MOCK_USERS = List.of(
            new User(1L, "Alice Silva", "alice.silva", "alice@email.com", "11999990001"),
            new User(2L, "Bruno Costa", "bruno.costa", "bruno@email.com", "11999990002"),
            new User(3L, "Camila Santos", "camila.santos", "camila@email.com", "11999990003")
    );

    @Override
    public Optional<User> findById(Long id) {
        return MOCK_USERS.stream()
                .filter(user -> user.id().equals(id))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return MOCK_USERS;
    }
}