package com.am.design.development.utilities.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    public void sendEmail(String to, String subject, String text, boolean htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(env.getProperty("spring.mail.username"));
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, htmlContent);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            LOGGER.error("Cannot send email", e);
            throw new RuntimeException(e);
        }
    }
}

