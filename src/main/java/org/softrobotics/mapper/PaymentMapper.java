package org.softrobotics.mapper;


import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.softrobotics.dto.PaymentDTO;
import org.softrobotics.entity.PaymentHistory;
import org.softrobotics.payment.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@ApplicationScoped
@Slf4j
public class PaymentMapper {

    public PaymentDTO.PaymentResponse providerResponseToPaymentResponse(PaymentDTO.ProviderResponse providerResponse){
        return PaymentDTO.PaymentResponse.builder()
                .gatewayTxnId(providerResponse.getGatewayTxnId())
                .providerTxnId(providerResponse.getProviderTxnId())
                .status(providerResponse.getStatus())
                .success(providerResponse.isSuccess())
                .providerTxnURL(providerResponse.getProviderTxnURL())
                .message(providerResponse.getMessage())
                .build();
    }

    public PaymentHistory paymentRequestToPaymentHistory(PaymentDTO.PaymentRequest request){
        return PaymentHistory.builder()
                .gatewayStatus(PaymentStatus.INITIATED.name())
                .currency(request.getCurrency())
                .amount(request.getAmount())
                .country(request.getCountry())
                .industry(request.getIndustry())
                .source(request.getPaymentSource())
                .gatewayTxnId(UUID.randomUUID().toString())
                .txnTime(LocalDateTime.now())
                .createdTime(LocalDateTime.now())
                .reference(request.getReference())
                .build();
    }
}
