package org.softrobotics.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.softrobotics.dto.PaymentDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentHistory  extends PanacheEntity {
    private String gatewayStatus;
    private String providerStatus;
    private String gatewayTxnId;
    private String providerTxnId;
    private String currency;
    private String country;
    private String industry;
    private String source;
    private BigDecimal amount;
    private String provider;
    private String reference;
    private LocalDateTime txnTime;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    @Column(name = "provider_response", columnDefinition = "TEXT")
    private String providerResponse;
}
