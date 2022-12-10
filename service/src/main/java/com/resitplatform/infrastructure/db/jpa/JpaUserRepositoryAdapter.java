 
package com.resitplatform.infrastructure.db.jpa;

import com.resitplatform.domain.model.User;
import com.resitplatform.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JpaUserRepositoryAdapter implements UserRepository {

    private final DataUserRepository repository;

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public Iterable<User> findAll() {
        return repository.findAll();
    }

}
