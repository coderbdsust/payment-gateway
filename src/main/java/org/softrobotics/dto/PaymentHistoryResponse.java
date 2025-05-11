package org.softrobotics.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PaymentHistoryResponse {
    public String gatewayStatus;
    public String providerStatus;
    public String gatewayTxnId;
    public String providerTxnId;
    public String currency;
    public String country;
    public String industry;
    public String source;
    public String reference;
    public BigDecimal amount;
    public String provider;
    public LocalDateTime txnTime;
}
