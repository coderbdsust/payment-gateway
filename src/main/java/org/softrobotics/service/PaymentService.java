package org.softrobotics.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.softrobotics.dto.PaymentDTO;
import org.softrobotics.entity.PaymentHistory;
import org.softrobotics.mapper.PaymentMapper;
import org.softrobotics.payment.PaymentStatus;
import org.softrobotics.payment.PaymentType;
import org.softrobotics.payment.provider.PaymentProvider;
import org.softrobotics.payment.provider.PaymentProviderSelector;
import org.softrobotics.payment.provider.PaypalPaymentProvider;
import org.softrobotics.payment.provider.StripePaymentProvider;
import org.softrobotics.repository.PaymentHistoryRepository;
import java.nio.file.ProviderNotFoundException;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;

@ApplicationScoped
@Slf4j
public class PaymentService {

    @Inject
    PaymentProviderSelector selector;

    @Inject
    PaymentHistoryRepository historyRepo;

    @Inject
    PaymentMapper paymentMapper;

    @Inject
    ExecutorService executor;

    @Transactional
    public PaymentDTO.PaymentResponse pay(PaymentDTO.PaymentRequest request) {
        log.info("Processing request {}", request);
        PaymentHistory history = paymentMapper.paymentRequestToPaymentHistory(request);
        PaymentProvider provider = selector.selectProvider(request);

        if(provider==null) {
            history.setProvider(PaymentType.Unknown.name());
            history.setGatewayStatus(PaymentStatus.FAILED.name());
            historyRepo.persist(history);
            log.info("No provider found for this request {}", request);
            return PaymentDTO.PaymentResponse.builder()
                    .success(false)
                    .status(PaymentStatus.FAILED.name())
                    .gatewayTxnId(history.getGatewayTxnId())
                    .message("Provider not found")
                    .build();
        }

        if(provider instanceof PaypalPaymentProvider)
            history.setProvider(PaymentType.Paypal.name());

        if(provider instanceof StripePaymentProvider)
            history.setProvider(PaymentType.Stripe.name());

        PaymentDTO.ProviderResponse response = provider.process(request, history.getGatewayTxnId());
        log.info("Provider response {}", response);
        history.setGatewayStatus(response.isSuccess() ? PaymentStatus.PAYMENT_INITIATED.name() : PaymentStatus.FAILED.name());
        history.setProviderTxnId( response.getProviderTxnId());
        history.setProviderStatus(response.getStatus());
        history.setProviderResponse(response.getProviderResponse());
        history.setUpdatedTime(LocalDateTime.now());
        historyRepo.persist(history);
        log.info("History updated for  Id {} ",history.id);
        return paymentMapper.providerResponseToPaymentResponse(response);
    }

    public CompletionStage<PaymentDTO.PaymentResponse> payAsync(PaymentDTO.PaymentRequest request) {
        return CompletableFuture.supplyAsync(() -> pay(request), executor);
    }


}
