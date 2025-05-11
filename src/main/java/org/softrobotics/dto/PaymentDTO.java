package org.softrobotics.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

public class PaymentDTO {

    @Data
    public static class PaymentRequest {
        private String country;
        private String industry;
        private String paymentSource;
        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
        private BigDecimal amount;
        @NotNull(message = "Currency is required")
        @Size(min = 3, max = 3, message = "Currency value must be 3 character long")
        private String currency;
        private String reference;
    }

    @Data
    @Builder
    public static class ProviderResponse {
        private boolean success;
        private String status;
        private String message;
        private String gatewayTxnId;
        private String providerTxnId;
        private String providerResponse;
        private String providerTxnURL;
    }

    @Data
    @Builder
    public static class PaymentResponse{
        private boolean success;
        private String status;
        private String message;
        private String gatewayTxnId;
        private String providerTxnId;
        private String providerTxnURL;

    }
}
