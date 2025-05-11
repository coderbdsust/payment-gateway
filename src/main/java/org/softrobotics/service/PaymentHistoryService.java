package org.softrobotics.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.softrobotics.dto.PaymentHistoryResponse;
import org.softrobotics.dto.PageResponse;
import org.softrobotics.entity.PaymentHistory;
import org.softrobotics.repository.PaymentHistoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Slf4j
public class PaymentHistoryService {

    @Inject
    private PaymentHistoryRepository paymentHistoryRepo;

    public PaymentHistoryResponse getPaymentHistoryByTxnId(String txnId){
        PaymentHistory history = paymentHistoryRepo.findByGatewayTxnIdOrProviderTxnId(txnId);
        if(history==null){
            throw new PaymentHistoryNotFoundException("Payment history not found using this txn id "+txnId);
        }
        return toPaymentHistoryToHistoryResponse(history);
    }

    public PageResponse<PaymentHistoryResponse> getPaymentHistoryByPage(int pageNo, int pageSize) {

        if(pageNo<=0) pageNo=1;

        List<PaymentHistory> txnList = paymentHistoryRepo.findPaymentHistoryByPage(pageNo-1, pageSize);
        long totalCount = paymentHistoryRepo.count();

        List<PaymentHistoryResponse> content = txnList.stream()
                .map(this::toPaymentHistoryToHistoryResponse)
                .collect(Collectors.toList());

        int totalPage = (int) Math.ceil((double) totalCount / pageSize);

        return PageResponse.<PaymentHistoryResponse>builder()
                .content(content)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(totalPage)
                .totalCount((int) totalCount)
                .build();
    }


    private PaymentHistoryResponse toPaymentHistoryToHistoryResponse(PaymentHistory history){
        return PaymentHistoryResponse.builder()
                .gatewayTxnId(history.getGatewayTxnId())
                .currency(history.getCurrency())
                .country(history.getCountry())
                .industry(history.getIndustry())
                .gatewayStatus(history.getGatewayStatus())
                .providerTxnId(history.getProviderTxnId())
                .providerStatus(history.getProviderStatus())
                .provider(history.getProvider())
                .source(history.getSource())
                .amount(history.getAmount())
                .reference(history.getReference())
                .txnTime(history.getTxnTime())
                .build();
    }
}
