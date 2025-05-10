package org.softrobotics.payment.provider;

import org.softrobotics.dto.PaymentDTO;

public interface PaymentProvider {
    boolean supports(PaymentDTO.PaymentRequest request);
    PaymentDTO.ProviderResponse process(PaymentDTO.PaymentRequest request, String localTxnId);
}
