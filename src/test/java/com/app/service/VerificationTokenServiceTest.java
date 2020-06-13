package com.app.service;

import com.app.entity.User;
import com.app.entity.VerificationToken;
import com.app.repository.VerificationTokenRepository;
import com.app.utils.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class VerificationTokenServiceTest {

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @InjectMocks
    private VerificationTokenService verificationTokenService;

    @Test
    public void shouldCreateVerificationToken() {
        //given
        User user = User.builder().build();
        VerificationToken verificationToken = new VerificationToken(user);

        //when
        Result result = verificationTokenService.createVerificationToken(verificationToken);

        //then
        verify(verificationTokenRepository, times(1)).save(any(VerificationToken.class));
        assertThat(verificationToken.getToken()).isNotBlank();
        assertThat(verificationToken.getUser()).isNotNull();
    }
}
