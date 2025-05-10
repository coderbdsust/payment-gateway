package org.softrobotics.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.softrobotics.dto.PageResponse;
import org.softrobotics.dto.PaymentHistoryDTO;
import org.softrobotics.service.PaymentHistoryService;

import java.util.Map;

@Path("/payment/history")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class PaymentHistoryResource {

    @Inject
    PaymentHistoryService historyService;

    /**
     *
     * @param txnId
     * Payment history can be accessed via gatewayTxnId or providerTxnId using this endpoint
     * @return
     */
    @GET
    @Path("/{txnId}")
    public Response historyByTxnId(String txnId) {
        log.debug("/payment/history/ : {}", txnId);
        try {
            PaymentHistoryDTO.HistoryResponse res = historyService.getPaymentHistoryByTxnId(txnId);
            return Response.ok(res).build();
        } catch (Exception e) {
            log.error("",e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    /**
     *
     * @param pageNo
     * @param rowSize
     * All the payment history is accessible using this endpoint using pagination params
     * @return
     */

    @GET
    public Response historyByPage(@QueryParam("pageNo") int pageNo, @QueryParam("rowSize") int rowSize) {
        log.debug("/payment/history/ : pageNo {}, rowSize {}  ", pageNo, rowSize);

        try {
            PageResponse<PaymentHistoryDTO.HistoryResponse> pageResponse= historyService.getPaymentHistoryByPage(pageNo, rowSize);
            return Response.ok(pageResponse).build();
        } catch (Exception e) {
            log.error("",e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }
}
