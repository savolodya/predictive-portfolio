package com.savolodya.predictiveportfolio.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class EmailService {
    @Value(value = "${url.finish-registration}")
    private String templateFinishRegisterUrl;

    public void sendRegisterAccountEmail(String email, UUID actionToken) {
        String finishRegisterUrl = String.format(templateFinishRegisterUrl, actionToken.toString());

        log.warn("Implement email sending");
        log.info("Imitating sending registration link {} to email {}", finishRegisterUrl, email);
    }

    public void sendRegisterAccountConfirmationEmail(String email) {
        log.warn("Implement email sending");
        log.info("Imitating sending confirmation of registration to email {}", email);
    }
}
