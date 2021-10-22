package com.app.verification;

import com.app.user.entity.User;
import com.app.verification.entity.VerificationToken;
import com.app.verification.repository.VerificationTokenRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    public VerificationToken createVerificationToken(User user) {
        return verificationTokenRepository.findByUser(user).orElseGet( () -> {
            VerificationToken verificationToken = new VerificationToken();
            verificationToken.setToken(UUID.randomUUID().toString());
            verificationToken.setUser(user);
            verificationTokenRepository.save(verificationToken);

            return verificationToken;
        });
    }
}
