package org.softrobotics.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.softrobotics.dto.PaymentDTO;
import org.softrobotics.service.PaymentService;

import java.util.Map;
import java.util.concurrent.CompletionStage;

@Path("/payment")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class PaymentResource {

    @Inject
    PaymentService paymentService;

    /**
     *
     * @param request
     * For payment this endpoint is used
     * When payment is initiated successfully  gateway-txn-id and provider-txn-id is returned
     * @return
     */

    @POST
    @Path("/pay")
    public Response pay(@Valid PaymentDTO.PaymentRequest request) {
        log.info("/payment/pay : {}", request);
        try {
            PaymentDTO.PaymentResponse response = paymentService.pay(request);
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
     * @param request
     * Payment is done in async way using this endpoint
     * @return
     */
    @POST
    @Path("/async/pay")
    public CompletionStage<Response> payAsync(@Valid PaymentDTO.PaymentRequest request) {
        log.info("/payment/async/pay : {}", request);
        return paymentService.payAsync(request)
                .thenApply(response -> Response.ok(response).build())
                .exceptionally(e -> {
                    log.error("Payment failed", e);
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(Map.of("error", e.getMessage()))
                            .build();
                });
    }

}
