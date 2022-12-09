package com.example.p2ptransaction.service;
import com.example.p2ptransaction.entity.User;
import com.example.p2ptransaction.service.interfaces.EmailSendOTPService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailSendOTPServiceImpl implements EmailSendOTPService {



    @Override
    public Integer otp() {
        Random random = new Random();

        return random.nextInt(900_000)+100_000;
    }

    @Override
    public boolean sendEmail(int generatePassword) {

        final String username = "ergashevq346@gmail.com";
        final String password = "xykgxgsrhxyolvkt";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        prop.put("mail.smtp.starttls.required", "true"); //TLS
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {

            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(user.getEmail())
            );
            message.setSubject("Quvonch");
            message.setText("Do not give the password to anyone.......!         password:   "+generatePassword+"");

            Transport.send(message);

            System.out.println("Done");
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
