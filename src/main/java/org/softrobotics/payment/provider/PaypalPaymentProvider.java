package org.softrobotics.payment.provider;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.softrobotics.dto.PaymentDTO;

@ApplicationScoped
@Slf4j
public class PaypalPaymentProvider implements PaymentProvider {

    @Override
    public boolean supports(PaymentDTO.PaymentRequest request) {
        return "paypal".equalsIgnoreCase(request.getSource()) || "EU".equalsIgnoreCase(request.getCountry()) ;
    }

    @Override
    public PaymentDTO.ProviderResponse process(PaymentDTO.PaymentRequest request, String gatewayTxnId) {
        // Need to simulate a PayPal API call
        log.debug("Processing payment via PayPal for: {}", request);
        return PaymentDTO.ProviderResponse.builder()
                .success(false)
                .status("Implementation Pending")
                .gatewayTxnId(gatewayTxnId)
                .build();
    }
}
