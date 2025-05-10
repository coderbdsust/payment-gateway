package org.softrobotics.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.softrobotics.dto.PaymentDTO;
import org.softrobotics.service.PaymentService;

import java.util.Map;

@Path("/payment")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class PaymentResource {

    @Inject
    PaymentService paymentService;

    @POST
    @Path("/pay")
    public Response pay(PaymentDTO.PaymentRequest request) {
        log.debug("/payment/pay : {}", request);
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
}
