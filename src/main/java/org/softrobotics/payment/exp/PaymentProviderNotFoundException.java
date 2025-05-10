package org.softrobotics.payment.exp;

public class PaymentProviderNotFoundException extends RuntimeException {

    public PaymentProviderNotFoundException(){}

    public PaymentProviderNotFoundException(String msg){
        super(msg);
    }
}
