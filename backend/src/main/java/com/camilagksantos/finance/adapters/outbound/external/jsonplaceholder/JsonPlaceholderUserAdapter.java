package com.camilagksantos.finance.adapters.outbound.external.jsonplaceholder;

import com.camilagksantos.finance.adapters.outbound.external.jsonplaceholder.client.JsonPlaceholderClient;
import com.camilagksantos.finance.domain.model.User;
import com.camilagksantos.finance.domain.ports.output.UserOutputPort;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Profile("prod")
public class JsonPlaceholderUserAdapter implements UserOutputPort {

    private final JsonPlaceholderClient jsonPlaceholderClient;

    public JsonPlaceholderUserAdapter(JsonPlaceholderClient jsonPlaceholderClient) {
        this.jsonPlaceholderClient = jsonPlaceholderClient;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(jsonPlaceholderClient.getUserById(id));
    }

    @Override
    public List<User> findAll() {
        return jsonPlaceholderClient.getUsers();
    }
}