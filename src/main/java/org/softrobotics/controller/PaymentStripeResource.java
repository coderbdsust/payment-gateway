package org.softrobotics.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.softrobotics.dto.PaymentDTO;
import org.softrobotics.service.PaymentService;
import org.softrobotics.service.StripeService;

import java.util.Map;

@Path("/payment")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class PaymentStripeResource {

    @Inject
    StripeService stripeService;

    /**
     *
     * @param txnId
     * Payment is done successfully via stripe UI, it's send the confirmation in this callback URL
     * @return
     */
    @GET
    @Path("/stripe/success")
    public Response success(@QueryParam("gatewayTxnId") String txnId) {
        log.info("/payment/success : {}", txnId);
        try {
            PaymentDTO.PaymentResponse response = stripeService.success(txnId);
            return Response.ok(response).build();
        } catch (Exception e) {
            log.error("",e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    /**
     *
     * @param txnId
     * Payment is cancelled via stripe UI, it's send the confirmation in this callback URL
     * @return
     */
    @GET
    @Path("/stripe/cancel")
    public Response cancel(@QueryParam("gatewayTxnId") String txnId) {
        log.info("/payment/cancel : {}", txnId);
        try {
            PaymentDTO.PaymentResponse response = stripeService.cancel(txnId);
            return Response.ok(response).build();
        } catch (Exception e) {
            log.error("",e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }
}
