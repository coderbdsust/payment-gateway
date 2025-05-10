package org.softrobotics.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.softrobotics.dto.PaymentDTO;
import org.softrobotics.entity.PaymentHistory;
import org.softrobotics.payment.PaymentStatus;
import org.softrobotics.payment.PaymentType;
import org.softrobotics.payment.provider.PaymentProvider;
import org.softrobotics.payment.provider.PaymentProviderSelector;
import org.softrobotics.payment.provider.PaypalPaymentProvider;
import org.softrobotics.payment.provider.StripePaymentProvider;
import org.softrobotics.repository.PaymentHistoryRepository;
import java.nio.file.ProviderNotFoundException;
import java.time.LocalDateTime;
import java.util.UUID;

@ApplicationScoped
@Slf4j
public class PaymentService {

    @Inject
    PaymentProviderSelector selector;

    @Inject
    PaymentHistoryRepository historyRepo;

    @Transactional
    public PaymentDTO.PaymentResponse pay(PaymentDTO.PaymentRequest request) {
        log.debug("Processing request {}", request);
        PaymentHistory history = requestToHistory(request);
        PaymentProvider provider = selector.selectProvider(request);

        if(provider==null) {
            history.setProvider("Unknown");
            historyRepo.persist(history);
            log.debug("No provider found for this request {}", request);
            throw new ProviderNotFoundException("No provider found for transaction for this request");
        }

        if(provider instanceof PaypalPaymentProvider)
            history.setProvider(PaymentType.Paypal.name());

        if(provider instanceof StripePaymentProvider)
            history.setProvider(PaymentType.Stripe.name());

        PaymentDTO.ProviderResponse response = provider.process(request, history.getGatewayTxnId());
        log.debug("Provider response {}", response);
        history.setGatewayStatus(response.isSuccess() ? PaymentStatus.PAYMENT_INITIATED.name() : PaymentStatus.FAILED.name());
        history.setProviderTxnId( response.getProviderTxnId());
        history.setProviderStatus(response.getStatus());
        history.setProviderResponse(response.getProviderResponse());
        history.setUpdatedTime(LocalDateTime.now());

        historyRepo.persist(history);
        log.debug("History updated for  Id {} ",history.id);
        return toPaymentResponse(response);
    }

    private PaymentDTO.PaymentResponse toPaymentResponse(PaymentDTO.ProviderResponse providerResponse){

       return PaymentDTO.PaymentResponse.builder()
                .gatewayTxnId(providerResponse.getGatewayTxnId())
                .providerTxnId(providerResponse.getProviderTxnId())
                .status(providerResponse.getStatus())
                .success(providerResponse.isSuccess())
                .providerTxnURL(providerResponse.getProviderTxnURL())
                .message(providerResponse.getMessage())
                .build();
    }

    private PaymentHistory requestToHistory(PaymentDTO.PaymentRequest request){
        return PaymentHistory.builder()
                .gatewayStatus(PaymentStatus.INITIATED.name())
                .currency(request.getCurrency())
                .amount(request.getAmount())
                .country(request.getCountry())
                .industry(request.getIndustry())
                .source(request.getSource())
                .gatewayTxnId(UUID.randomUUID().toString())
                .txnTime(LocalDateTime.now())
                .createdTime(LocalDateTime.now())
                .reference(request.getReference())
                .build();
    }
}
