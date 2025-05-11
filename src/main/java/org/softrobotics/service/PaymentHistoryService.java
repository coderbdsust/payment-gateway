package org.softrobotics.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.softrobotics.dto.PageResponse;
import org.softrobotics.dto.PaymentHistoryDTO;
import org.softrobotics.entity.PaymentHistory;
import org.softrobotics.repository.PaymentHistoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Slf4j
public class PaymentHistoryService {

    @Inject
    private PaymentHistoryRepository paymentHistoryRepo;

    public PaymentHistoryDTO.HistoryResponse getPaymentHistoryByTxnId(String txnId){
        PaymentHistory history = paymentHistoryRepo.findByGatewayTxnIdOrProviderTxnId(txnId);
        return toPaymentHistoryToHistoryResponse(history);
    }

    public PageResponse<PaymentHistoryDTO.HistoryResponse> getPaymentHistoryByPage(int pageNo, int pageSize) {

        if(pageNo<=0) pageNo=1;

        List<PaymentHistory> txnList = paymentHistoryRepo.findPaymentHistoryByPage(pageNo-1, pageSize);
        long totalCount = paymentHistoryRepo.count();

        List<PaymentHistoryDTO.HistoryResponse> content = txnList.stream()
                .map(this::toPaymentHistoryToHistoryResponse)
                .collect(Collectors.toList());

        int totalPage = (int) Math.ceil((double) totalCount / pageSize);

        return PageResponse.<PaymentHistoryDTO.HistoryResponse>builder()
                .content(content)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(totalPage)
                .totalCount((int) totalCount)
                .build();
    }


    private PaymentHistoryDTO.HistoryResponse toPaymentHistoryToHistoryResponse(PaymentHistory history){
        return PaymentHistoryDTO.HistoryResponse.builder()
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
