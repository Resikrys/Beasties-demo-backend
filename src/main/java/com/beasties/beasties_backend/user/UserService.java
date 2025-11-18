package com.beasties.beasties_backend.user;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    Long findIdByUsername(String username);
}
