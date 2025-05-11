package org.softrobotics.payment.provider;

import org.softrobotics.dto.PaymentDTO;

public interface PaymentProvider {
    boolean supportPaymentSource(PaymentDTO.PaymentRequest request);
    boolean supportCountry(PaymentDTO.PaymentRequest request);
    boolean supportIndustry(PaymentDTO.PaymentRequest request);
    PaymentDTO.ProviderResponse process(PaymentDTO.PaymentRequest request, String localTxnId);
}
