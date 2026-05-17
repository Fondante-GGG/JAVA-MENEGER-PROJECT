package com.example.kur.infrastructure.persistence.adapter;

import com.example.kur.domain.model.Email;
import com.example.kur.domain.model.UserAccount;
import com.example.kur.domain.port.UserAccountRepository;
import com.example.kur.infrastructure.persistence.jpa.entity.UserJpaEntity;
import com.example.kur.infrastructure.persistence.jpa.repo.UserJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserAccountRepositoryAdapter implements UserAccountRepository {
    private final UserJpaRepository repo;

    public UserAccountRepositoryAdapter(UserJpaRepository repo) {
        this.repo = repo;
    }

    @Override
    public Optional<UserAccount> findByEmail(Email email) {
        return repo.findByEmail(email.value()).map(this::toDomain);
    }

    @Override
    public void save(UserAccount userAccount) {
        repo.save(toJpa(userAccount));
    }

    private UserAccount toDomain(UserJpaEntity e) {
        return new UserAccount(e.getId(), new Email(e.getEmail()), e.getPasswordHash(), e.getRole(), e.isEnabled());
    }

    private UserJpaEntity toJpa(UserAccount u) {
        return new UserJpaEntity(u.id(), u.email().value(), u.passwordHash(), u.role(), u.enabled());
    }
}

