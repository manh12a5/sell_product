package com.example.demo.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService implements EmailSender {
    private static final String ENCODING = "UTF-8";
    private static final String CONTENT_TYPE = "text/html; charset=utf-8";

    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.mail.sender:manh12a5@gmail.com}")
    private String fromEmail;

    @Override
    @Async
    public void send(String to, Map<String, Object> templateAttributes, String templateForm) {
        try {
            Context context = prepareEvaluationContext(templateAttributes);
            String htmlBodyContent = templateEngine.process(templateForm, context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, ENCODING);

            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject("Confirm your email");
            mimeMessageHelper.setFrom(fromEmail);

            Multipart multipart = new MimeMultipart();
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setHeader("Content-Type", "text/html");
            mimeBodyPart.setContent(htmlBodyContent, CONTENT_TYPE);
            multipart.addBodyPart(mimeBodyPart);

            mimeMessage.setContent(multipart);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Failed to send email");
            throw new IllegalStateException("Failed to send email");
        }
    }

    private Context prepareEvaluationContext(Map<String, Object> templateAttributes) {
        final Context evaluationContext = new Context();
        if (templateAttributes != null) {
            for (Map.Entry<String, Object> attribute : templateAttributes.entrySet()) {
                evaluationContext.setVariable(attribute.getKey(), attribute.getValue());
            }
        }

        return evaluationContext;
    }
}
