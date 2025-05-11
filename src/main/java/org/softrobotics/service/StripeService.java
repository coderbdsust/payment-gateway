package org.softrobotics.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.softrobotics.dto.PaymentDTO;
import org.softrobotics.entity.PaymentHistory;
import org.softrobotics.payment.PaymentStatus;
import org.softrobotics.repository.PaymentHistoryRepository;

@ApplicationScoped
@Slf4j
public class StripeService {

    @Inject
    PaymentHistoryRepository paymentHistoryRepo;

    @ConfigProperty(name = "payment.gateway.stripe.secret.key")
    String stripeSecretKey;

    /**
     *
     * @param txnId
     * @return Payment is done using stripe transaction URL,
     * After payment it's confirm in this call back URL and
     * Using gateway txn id it retrieve the session and
     * payment history update the payment id and transaction status
     * @throws StripeException
     */

    @Transactional
    public PaymentDTO.PaymentResponse success(String txnId) throws StripeException {
       PaymentHistory paymentHistory =  paymentHistoryRepo.findByGatewayTxnIdOrProviderTxnId(txnId);

        if(paymentHistory.getGatewayStatus().equals(PaymentStatus.SUCCESS.name())){
            return PaymentDTO.PaymentResponse.builder()
                    .success(true)
                    .status(PaymentStatus.SUCCESS.name())
                    .message("Payment Successful")
                    .gatewayTxnId(txnId)
                    .providerTxnId(paymentHistory.getProviderTxnId())
                    .build();
        }

        Stripe.apiKey = stripeSecretKey;
        Session retrieve = Session.retrieve(paymentHistory.getProviderTxnId());
        if(retrieve!=null && retrieve.getPaymentIntent()!=null){
            String paymentId = retrieve.getPaymentIntent();
            paymentHistory.setProviderTxnId(paymentId);
            paymentHistory.setGatewayStatus(PaymentStatus.SUCCESS.name());
            paymentHistory.setProviderStatus(retrieve.getStatus());
            paymentHistoryRepo.persist(paymentHistory);
            return PaymentDTO.PaymentResponse.builder()
                    .success(true)
                    .status(PaymentStatus.SUCCESS.name())
                    .message("Payment Successful")
                    .gatewayTxnId(txnId)
                    .providerTxnId(paymentHistory.getProviderTxnId())
                    .build();
        }else{
            return PaymentDTO.PaymentResponse.builder()
                    .success(false)
                    .status(PaymentStatus.ERROR.name())
                    .message("Payment Couldn't Update")
                    .gatewayTxnId(txnId)
                    .providerTxnId(paymentHistory.getProviderTxnId())
                    .build();
        }
    }

    @Transactional
    public PaymentDTO.PaymentResponse cancel(String txnId) {
        PaymentHistory paymentHistory =  paymentHistoryRepo.findByGatewayTxnIdOrProviderTxnId(txnId);
        paymentHistory.setGatewayStatus(PaymentStatus.CANCELED.name());
        paymentHistoryRepo.persist(paymentHistory);
        return PaymentDTO.PaymentResponse.builder()
                .success(false)
                .status(PaymentStatus.CANCELED.name())
                .message("Payment Canceled")
                .gatewayTxnId(txnId)
                .build();
    }
}
