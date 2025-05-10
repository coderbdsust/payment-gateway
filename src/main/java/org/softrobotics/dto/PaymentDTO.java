package org.softrobotics.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

public class PaymentDTO {

    @Data
    public static class PaymentRequest {
        private String country;
        private String industry;
        private String source;
        private BigDecimal amount;
        private String currency;
    }

    @Data
    @Builder
    public static class ProviderResponse {
        private boolean success;
        private String status;
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
