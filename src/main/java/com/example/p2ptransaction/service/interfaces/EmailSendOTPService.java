package com.example.p2ptransaction.service.interfaces;

public interface EmailSendOTPService {

    Integer otp();

    boolean sendEmail(int password);


}
