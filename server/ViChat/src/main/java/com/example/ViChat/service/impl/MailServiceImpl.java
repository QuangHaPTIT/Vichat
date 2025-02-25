package com.example.ViChat.service.impl;

import com.example.ViChat.dto.request.MailRequest;
import com.example.ViChat.service.MailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {
    private final JavaMailSender javaMailSender;

    private final SpringTemplateEngine templateEngine;

    private final ObjectMapper objectMapper;
    @Override
    public void sendHtmlMail(MailRequest mailRequest, String templateName) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

        Context context = new Context();
        context.setVariables(mailRequest.getProps());
        String html = templateEngine.process(templateName, context);

        helper.setTo(mailRequest.getTo());
        helper.setSubject(mailRequest.getSubject());
        helper.setText(html, true);
        javaMailSender.send(message);
    }

    @KafkaListener(topics = {"confirm-account-topic", "forgot-password-topic"}, groupId = "confirm-account-group")
    public void sendHtmlMailByKafka(Map<String, Object> message) throws MessagingException {
        MailRequest mailRequest = objectMapper.convertValue(message.get("mailRequest"), MailRequest.class);
        String templateName = (String) message.get("template");
        MimeMessage messageMime = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(messageMime, true, "utf-8");

        Context context = new Context();
        context.setVariables(mailRequest.getProps());
        String html = templateEngine.process(templateName, context);

        helper.setTo(mailRequest.getTo());
        helper.setSubject(mailRequest.getSubject());
        helper.setText(html, true);
        javaMailSender.send(messageMime);
    }
}
