package org.softrobotics.service;

public class PaymentHistoryNotFoundException extends RuntimeException {
    public PaymentHistoryNotFoundException(String s) {
        super(s);
    }
}
