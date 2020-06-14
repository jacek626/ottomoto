package com.app.service;

import com.app.entity.User;
import com.app.entity.VerificationToken;
import com.app.repository.VerificationTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class VerificationTokenServiceTest {

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @InjectMocks
    private VerificationTokenService verificationTokenService;

    @Test
    public void shouldCreateNewVerificationToken() {
        //given
        User user = User.builder().build();
        when(verificationTokenRepository.findByUser(any(User.class))).thenReturn(Optional.empty());

        //when
        VerificationToken verificationToken = verificationTokenService.createVerificationToken(user);

        //then
        verify(verificationTokenRepository, times(1)).save(any(VerificationToken.class));
        assertThat(verificationToken.getToken()).isNotBlank();
        assertThat(verificationToken.getUser()).isNotNull();
    }

    @Test
    public void shouldUseExistingVerificationToken() {
        //given
        User user = User.builder().build();

        //when
        VerificationToken verificationToken = verificationTokenService.createVerificationToken(user);

        //then
        verify(verificationTokenRepository, times(0)).save(any(VerificationToken.class));
        assertThat(verificationToken.getToken()).isNotBlank();
        assertThat(verificationToken.getUser()).isNotNull();
    }
}
