package com.example.p2ptransaction.service;

import com.example.p2ptransaction.dto.*;
import com.example.p2ptransaction.entity.*;
import com.example.p2ptransaction.payload.ResponseApi;
import com.example.p2ptransaction.repositories.TransactionRepository;
import com.example.p2ptransaction.service.interfaces.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    @Value("${app.uzcardConnection}")
    private String urlUzcard;
    @Value("${app.humoConnection}")
    private String urlHumo;
    private final EmailSendOTPServiceImpl emailSendOTPService;
    private final OTPTransactionServiceImpl otpTransactionService;
    private final CardServiceImpl cardService;

    private final TransactionRepository transactionRepository;

    private final EOPSServiceImpl eopsService;
    @Override
    public Map<String, String> checkCard(Long cardId) {
        Optional<Card> byId = cardService.findById(cardId);
        if (byId.isEmpty()) {
            return Map.of(
                    "ok", "",
                    "msg", "not found"
            );
        }
        int year = ((LocalDate.now().getYear() / 100) * 100) + Integer.parseInt(byId.get().getExpireDate().substring(3));
        int month = Integer.parseInt(byId.get().getExpireDate().substring(0, 2));
        LocalDate of = LocalDate.of(year, month + 1, 1);
        if (of.isBefore(LocalDate.now())) {
            return Map.of(
                    "ok", "",
                    "msg", "The card has expired.....!"
            );
        }
        return Map.of(
                "ok", "ok",
                "token", byId.get().getToken(),
                "cardMask", byId.get().getCardMask()
        );
    }
    @Override
    public Map<String, String> findByToken(Long cardId) {
        Map<String, String> stringStringMap = checkCard(cardId);
        if (stringStringMap.get("ok").equals("")) {
            return Map.of(
                    "ok", "",
                    "msg", "not found"
            );
        }
        RestTemplate restTemplate = new RestTemplate();
        CardDto cardDto = new CardDto();
        if (stringStringMap.get("cardMask").startsWith("8600")) {
            cardDto = restTemplate.getForObject(urlUzcard + "/find-by-token/" + stringStringMap.get("token"), CardDto.class);
        } else if (stringStringMap.get("cardMask").startsWith("9860")) {
            cardDto = restTemplate.getForObject(urlHumo + "/find-by-token/" + stringStringMap.get("token"), CardDto.class);
        }
        return Map.of(
                "ok", "ok",
                "connectionPhoneNumber", cardDto.getConnectionPhoneNumber(),
                "token", stringStringMap.get("token")
        );
    }
    @Override
    public boolean checkPhoneNumber(Long cardId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, String> byToken = findByToken(cardId);
        if (byToken.get("ok").equals("")) {
            return false;
        }
        if (byToken.get("connectionPhoneNumber").equals(user.getPhoneNumber())) {
            return true;
        }
        return false;
    }
    @Override
    public EmailDto transactionEmailMessage(TransactionDto transactionDto) {
        boolean checkPhoneNumber = checkPhoneNumber(transactionDto.getGetMoneyCardId());
        Optional<Card> cardOptional = cardService.findById(transactionDto.getGetMoneyCardId());
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getEmail();
        if (checkPhoneNumber) {
            Integer otp = emailSendOTPService.otp();
            if (emailSendOTPService.sendEmail(otp)) {
                LocalTime now = LocalTime.now().plusSeconds(600);
                OTPTransaction otpTransaction = new OTPTransaction();
                otpTransaction.setOtp(otp);
                otpTransaction.setDate(now);
                otpTransaction.setNote(cardOptional.get().getToken());
                otpTransaction.setAmount(transactionDto.getAmountTransfer());
                otpTransaction.setCardNumber(transactionDto.getGiveMoneyCardNumber());
                otpTransactionService.save(otpTransaction);
                return new EmailDto("Verification code send to " + email.substring(0, 7) + "******" + email.substring(email.length() - 7), now);
            }
        }
        return new EmailDto("Not found email....!", null);
    }

    @Override
    public Map<String, String> checkVerificationCode(VerificationCodeDto verificationCodeDto) {
        Optional<OTPTransaction> byOTP = otpTransactionService.findById(verificationCodeDto.getId());
        if (byOTP.isEmpty()) {
            return Map.of(
                    "ok", "",
                    "msg", "Not found verification password....!"

            );
        }
        if (byOTP.get().getDate().isBefore(LocalTime.now())) {
            return Map.of(
                    "ok", "",
                    "msg", "Verification code is outdated....!"
            );
        }
        if (byOTP.get().getAttempts() >= 3) {
            return Map.of(
                    "ok", "",
                    "msg", "Attempts finished.....!"
            );
        }
        if (!byOTP.get().getOtp().equals(verificationCodeDto.getVerificationCode())) {
            Integer attempts = byOTP.get().getAttempts();
            byOTP.get().setAttempts(attempts++);
            otpTransactionService.save(byOTP.get());
            return Map.of(
                    "ok", "",
                    "msg", "Password is wrong.Please try again...!"
            );
        }
        if (byOTP.get().isActive()) {
            return Map.of(
                    "ok", "ok",
                    "token", byOTP.get().getNote(),
                    "amount", byOTP.get().getAmount().toString(),
                    "cardNumber", byOTP.get().getCardNumber()
            );
        }

        return Map.of(
                "ok", "",
                "msg", "Verification code not active...!"
        );
    }

    @Override
    public ResponseApi transaction(VerificationCodeDto verificationCodeDto) {
        Map<String, String> stringSerializableMap = checkVerificationCode(verificationCodeDto);
        RestTemplate restTemplate=new RestTemplate();
        CheckTransactionDto giveCheckTransactionDto=new CheckTransactionDto();
        CheckTransactionDto getCheckTransactionDto=new CheckTransactionDto();
        if(stringSerializableMap.get("ok").equals("")){
            return new ResponseApi(stringSerializableMap.get("msg"), false);
        }
        Long amount = Long.valueOf(stringSerializableMap.get("amount"));

        GiveMoneyDto giveMoneyDto=new GiveMoneyDto();
        giveMoneyDto.setCardToken(stringSerializableMap.get("token"));
        giveMoneyDto.setAmountTransfer(amount);
        giveMoneyDto.setEops(eopsService.findById(1L).get());

        GetMoneyDto getMoneyDto=new GetMoneyDto();
        getMoneyDto.setAmountTransfer(amount);
        getMoneyDto.setCardNumber(stringSerializableMap.get("cardNumber"));
        getMoneyDto.setEops(eopsService.findById(1L).get());
        Card card = cardService.findByToken(stringSerializableMap.get("token"));

        if (card.getCardMask().startsWith("8600")) {
            giveCheckTransactionDto=restTemplate.postForObject(urlUzcard+"/give-money", giveMoneyDto, CheckTransactionDto.class);
        }
        if(card.getCardMask().startsWith("9860")){
            giveCheckTransactionDto=restTemplate.postForObject(urlHumo+"/give-money", giveMoneyDto, CheckTransactionDto.class);
        }
        if(giveCheckTransactionDto.getStatus().equals("SUCCEED")) {
            if (stringSerializableMap.get("cardNumber").startsWith("8600")) {
                getCheckTransactionDto=restTemplate.postForObject(urlUzcard + "/get-money", getMoneyDto, CheckTransactionDto.class);
            }
            if (stringSerializableMap.get("cardNumber").startsWith("9860")) {
                getCheckTransactionDto=restTemplate.postForObject(urlHumo + "/get-money", getMoneyDto, CheckTransactionDto.class);
            }
        }
        if (getCheckTransactionDto.getStatus().equals("SUCCEED")){
            Transaction transaction=new Transaction();
            transaction.setAmount(Long.valueOf(stringSerializableMap.get("amount")));
            transaction.setSendCardMask(stringSerializableMap.get("cardNumber"));
            transaction.setDate(LocalDateTime.now());
            transaction.setCardId(card.getId());
            transaction.setStatus(getCheckTransactionDto.getStatus());
            transactionRepository.save(transaction);

            return new ResponseApi("Successfully transaction...!", true);
        }
        return new ResponseApi("Failed transaction....!" ,false);
    }




}
