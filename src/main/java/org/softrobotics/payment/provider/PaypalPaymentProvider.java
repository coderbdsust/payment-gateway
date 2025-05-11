package org.softrobotics.payment.provider;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.softrobotics.dto.PaymentDTO;
import org.softrobotics.payment.PaymentStatus;
import org.softrobotics.payment.PaymentType;

@ApplicationScoped
@Slf4j
public class PaypalPaymentProvider implements PaymentProvider {

    @Override
    public boolean supportPaymentSource(PaymentDTO.PaymentRequest request) {
        return PaymentType.Paypal.name().equalsIgnoreCase(request.getPaymentSource());
    }

    @Override
    public boolean supportCountry(PaymentDTO.PaymentRequest request) {
        return "EU".equalsIgnoreCase(request.getCountry()) ;
    }

    @Override
    public boolean supportIndustry(PaymentDTO.PaymentRequest request) {
        return false;
    }

    @Override
    public PaymentDTO.ProviderResponse process(PaymentDTO.PaymentRequest request, String gatewayTxnId) {
        log.info("Processing payment via PayPal for: {}", request);
        return PaymentDTO.ProviderResponse.builder()
                .success(false)
                .status(PaymentStatus.FAILED.name())
                .message("Implementation Pending for Paypal")
                .gatewayTxnId(gatewayTxnId)
                .build();
    }
}
