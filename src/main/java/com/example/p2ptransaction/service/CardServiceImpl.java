package com.example.p2ptransaction.service;

import com.example.p2ptransaction.dto.*;
import com.example.p2ptransaction.entity.*;
import com.example.p2ptransaction.payload.ResponseApi;
import com.example.p2ptransaction.repositories.CardRepository;
import com.example.p2ptransaction.service.interfaces.CardService;
import com.example.p2ptransaction.service.interfaces.EmailSendOTPService;
import com.example.p2ptransaction.service.interfaces.OTPCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    @Value("${app.uzcardConnection}")
    private String urlUzcard;
    @Value("${app.humoConnection}")
    private String urlHumo;
    private final EmailSendOTPService emailSendOTPService;
    private final OTPCardService otpCardService;
    private final CardRepository cardRepository;

    @Override
    public FindByIdCardDataDto findByCard(String cardNumber) {
        RestTemplate restTemplate = new RestTemplate();
        if (cardNumber.startsWith("8600")) {
            return restTemplate.getForObject(urlUzcard + "/find-by-transaction-card-number/" + cardNumber, FindByIdCardDataDto.class);
        } else if (cardNumber.startsWith("9860")) {
            return restTemplate.getForObject(urlHumo + "/find-by-transaction-card-number/" + cardNumber, FindByIdCardDataDto.class);
        }
        return new FindByIdCardDataDto(null, "Not found card....!", null);
    }

    @Override
    public EmailDto addCardEmailMessage(AddCardDto addCardDto) {
        RestTemplate restTemplate = new RestTemplate();
        CardDto cardDto = new CardDto();
        int year = ((LocalDate.now().getYear() / 100) * 100) + Integer.parseInt(addCardDto.getValidityPeriod().substring(3));
        int month = Integer.parseInt(addCardDto.getValidityPeriod().substring(0, 2));
        LocalDate of = LocalDate.of(year, month + 1, 1);

        if (of.isBefore(LocalDate.now())) {
            return new EmailDto("The card has expired.....!", null);
        }

        if (addCardDto.getCardNumber().startsWith("8600")) {
            cardDto = restTemplate.getForObject(urlUzcard + "/find-by-card-number/" + addCardDto.getCardNumber(), CardDto.class);
        } else if (addCardDto.getCardNumber().startsWith("9860")) {
            cardDto = restTemplate.getForObject(urlHumo + "/find-by-card-number/" + addCardDto.getCardNumber(), CardDto.class);
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getEmail();
        if (cardDto.getConnectionPhoneNumber().equals(user.getPhoneNumber())) {
            Integer otp = emailSendOTPService.otp();
            if (emailSendOTPService.sendEmail(otp)) {
                LocalTime localTime = LocalTime.now().plusSeconds(600);
                OTPCard otpCard = new OTPCard();
                otpCard.setDate(localTime);
                otpCard.setOtp(otp);
                otpCard.setNote(cardDto.getCardMask());
                otpCard.setCardBalance(cardDto.getBalance());
                otpCard.setExpireDate(cardDto.getValidityPeriod());
                otpCardService.save(otpCard);
                return new EmailDto("verification code send to " + email.substring(0, 4) + "*******" + email.substring(email.length() - 7), localTime);
            }
        }
        return new EmailDto("Not found email", null);
    }

    @Override
    public ResponseApi addCard(VerificationCodeDto verificationCode) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<OTPCard> byOTP = otpCardService.findById(verificationCode.getId());
        if (byOTP.isEmpty()) {
            return new ResponseApi("Not found verification password....!", false);
        }
        if (byOTP.get().getDate().isBefore(LocalTime.now())) {
            return new ResponseApi("Verification code is outdated....!", false);
        }
        if (byOTP.get().getAttempts() >= 3) {
            return new ResponseApi("Attempts finished.....!", false);
        }
        if (!byOTP.get().getOtp().equals(verificationCode.getVerificationCode())) {
            int attempts = byOTP.get().getAttempts();
            byOTP.get().setAttempts(++attempts);
            otpCardService.save(byOTP.get());
            return new ResponseApi("Password is wrong.Please try again...!", false);
        }
        if (byOTP.get().isActive()) {
            Card card = new Card();
            card.setCardMask(byOTP.get().getNote());
            card.setUserId(user.getId());
            RestTemplate restTemplate = new RestTemplate();
            TokenDto tokenDto = new TokenDto();
            if (byOTP.get().getNote().startsWith("8600")) {
                tokenDto = restTemplate.getForObject(urlUzcard + "/add-card-for-generate-token/" + byOTP.get().getNote(), TokenDto.class);
            }
            if (byOTP.get().getNote().startsWith("9860")) {
                tokenDto = restTemplate.getForObject(urlHumo + "/add-card-for-generate-token/" + byOTP.get().getNote(), TokenDto.class);
            }
            card.setToken(tokenDto.getToken());
            card.setBalance(byOTP.get().getCardBalance());
            card.setExpireDate(byOTP.get().getExpireDate());
            cardRepository.save(card);
            byOTP.get().setActive(false);
            otpCardService.save(byOTP.get());
            return new ResponseApi("Successfully added card....!", true);
        }
        return new ResponseApi("Verification code not active....!", false);
    }

    @Override
    public Optional<Card> findById(Long id) {
        return cardRepository.findById(id);
    }

    @Override
    public Card findByToken(String token) {
        return cardRepository.findByToken(token);
    }

    @Override
    public List<Card> findAllByClientId(Long userId) {
        List<Card> byUserId = cardRepository.findAllByUserId(userId);
        RestTemplate restTemplate = new RestTemplate();
        for (Card card : byUserId) {
            if (card.getCardMask().startsWith("8600")) {
                Long forObject = restTemplate.getForObject(urlUzcard + "/refresh/" + card.getToken(), Long.class);
                card.setBalance(forObject);
                cardRepository.save(card);
            }else if(card.getCardMask().startsWith("9860")) {
                Long forObject = restTemplate.getForObject(urlHumo + "/refresh/" + card.getToken(), Long.class);
                card.setBalance(forObject);
                cardRepository.save(card);
            }
        }
        return cardRepository.findAllByUserId(userId);
    }
}
