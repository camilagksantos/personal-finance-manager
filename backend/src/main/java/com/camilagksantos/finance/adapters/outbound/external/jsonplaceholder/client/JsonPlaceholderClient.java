package com.camilagksantos.finance.adapters.outbound.external.jsonplaceholder.client;

import com.camilagksantos.finance.domain.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "jsonplaceholder", url = "https://jsonplaceholder.typicode.com")
public interface JsonPlaceholderClient {

    @GetMapping("/users")
    List<User> getUsers();

    @GetMapping("/users/{id}")
    User getUserById(@PathVariable("id") Long id);
}