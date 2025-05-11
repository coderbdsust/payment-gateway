package org.softrobotics;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class PaymentResourceTest {

    @Test
    void testPaymentHistory() {
        given()
          .when().get("/payment/history?pageNo=1&rowSize=10")
          .then()
             .statusCode(200);
    }

    @Test
    void testPaymentHistoryByTxnId() {
        String txnId = UUID.randomUUID().toString();
        given()
                .when().get("/payment/history/"+txnId)
                .then()
                .statusCode(400);
    }

    @Test
    void testValidityCheckPaymentRequest(){
        Map<String, Object> paymentRequest = new HashMap<>();
        given()
                .contentType(ContentType.JSON)
                .body(paymentRequest)
                .when()
                .post("/payment/pay")
                .then()
                .statusCode(400);
    }

    @Test
    void testPaymentInitiationWithPaypalProvider(){
        Map<String, Object> paymentRequest = new HashMap<>();
        paymentRequest.put("paymentSource","Paypal");
        paymentRequest.put("currency","USD");
        paymentRequest.put("country", "US");
        paymentRequest.put("amount", "10");
        paymentRequest.put("industry","Testing Industry");

        given()
                .contentType(ContentType.JSON)
                .body(paymentRequest)
                .when()
                .post("/payment/pay")
                .then()
                .statusCode(200);
    }
}