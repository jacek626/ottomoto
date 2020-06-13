package com.app.service;

import com.app.entity.User;
import com.app.entity.VerificationToken;
import com.app.repository.VerificationTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    public VerificationToken createVerificationToken(User user) {
        Optional<VerificationToken> verificationTokenFromDB = verificationTokenRepository.findByUser(user);

        if (verificationTokenFromDB.isPresent())
            return verificationTokenFromDB.get();
        else {
            VerificationToken verificationToken = new VerificationToken();
            verificationToken.setToken(UUID.randomUUID().toString());
            verificationToken.setUser(user);
            verificationTokenRepository.save(verificationToken);

            return verificationToken;
        }
    }
}
