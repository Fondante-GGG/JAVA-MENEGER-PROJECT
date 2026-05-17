package com.example.kur.domain.port;

import com.example.kur.domain.model.Email;
import com.example.kur.domain.model.UserAccount;

import java.util.Optional;

public interface UserAccountRepository {
    Optional<UserAccount> findByEmail(Email email);

    void save(UserAccount userAccount);
}

