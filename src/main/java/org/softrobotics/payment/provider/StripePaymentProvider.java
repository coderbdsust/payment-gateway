package org.softrobotics.payment.provider;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.softrobotics.dto.PaymentDTO;
import org.softrobotics.payment.PaymentStatus;
import org.softrobotics.payment.PaymentType;

import java.math.BigDecimal;

@ApplicationScoped
@Slf4j
public class StripePaymentProvider implements PaymentProvider {

    @ConfigProperty(name = "payment.gateway.stripe.secret.key")
    String stripeSecretKey;

    @ConfigProperty(name = "payment.gateway.stripe.redirect.url")
    String stripeRedirectBaseURL;

    @Override
    public boolean supportPaymentSource(PaymentDTO.PaymentRequest request) {
        return PaymentType.Stripe.name().equalsIgnoreCase(request.getPaymentSource());
    }

    @Override
    public boolean supportCountry(PaymentDTO.PaymentRequest request) {
        return "US".equalsIgnoreCase(request.getCountry()) ;
    }

    @Override
    public boolean supportIndustry(PaymentDTO.PaymentRequest request) {
        return false;
    }

    @Override
    public PaymentDTO.ProviderResponse process(PaymentDTO.PaymentRequest request, String gatewayTxnId) {
        log.info("Processing payment via Stripe for: {}", request);

        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(request.getIndustry())
                        .build();

        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency(request.getCurrency().toLowerCase())
                        .setUnitAmount(request.getAmount().multiply(BigDecimal.valueOf(100)).longValue())
                        .setProductData(productData)
                        .build();

        SessionCreateParams.LineItem lineItem =
                SessionCreateParams.LineItem.builder()
                        .setPriceData(priceData)
                        .setQuantity(1L)
                        .build();

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl(stripeRedirectBaseURL+"/api/v1/payment/stripe/success?gatewayTxnId="+gatewayTxnId)
                        .setCancelUrl(stripeRedirectBaseURL+"/api/v1/payment/stripe/cancel?gatewayTxnId="+gatewayTxnId)
                        .addLineItem(lineItem)
                        .setPaymentIntentData(
                            SessionCreateParams.PaymentIntentData.builder()
                                    .putMetadata("payment_gateway_id", gatewayTxnId)
                                    .putMetadata("reference", request.getReference())
                                    .build()
                        ).build();

        try {
            Session session = Session.create(params);
            log.info("Stripe session created: {}", session.getId());
            log.info("Client ref id: {}",session.getClientReferenceId());

            return PaymentDTO.ProviderResponse.builder()
                    .success(true)
                    .status(session.getStatus())
                    .gatewayTxnId(gatewayTxnId)
                    .providerTxnId(session.getId())
                    .providerResponse(session.toJson())
                    .providerTxnURL(session.getUrl())
                    .build();

        } catch (StripeException e) {
            log.error("Stripe payment error: {}", e.getMessage(), e);
            return PaymentDTO.ProviderResponse.builder()
                    .success(false)
                    .status(PaymentStatus.ERROR.name())
                    .message(e.getMessage())
                    .gatewayTxnId(gatewayTxnId)
                    .providerResponse(e.getMessage())
                    .build();
        }
    }

}

