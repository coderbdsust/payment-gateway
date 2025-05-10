package org.softrobotics.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.softrobotics.entity.PaymentHistory;

import java.util.List;

@ApplicationScoped
public class PaymentHistoryRepository implements PanacheRepository<PaymentHistory> {

    public PaymentHistory findByGatewayTxnIdOrProviderTxnId(String txnId) {
        return find("gatewayTxnId = ?1 or providerTxnId = ?1", txnId).firstResult();
    }

    public List<PaymentHistory> findPaymentHistoryByPage(int pageNo, int rowSize) {
        return findAll()
                .page(pageNo, rowSize)
                .list();
    }

}
