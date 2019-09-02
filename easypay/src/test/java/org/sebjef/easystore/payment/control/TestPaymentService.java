/*
 * Copyright 2019 JF James.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sebjef.easystore.payment.control;

import io.quarkus.test.junit.QuarkusTest;
import org.sebjef.easypay.entity.PaymentResponseCode;
import org.sebjef.easypay.control.PaymentProcessingContext;
import org.sebjef.easypay.control.PaymentService;
import org.sebjef.easypay.boundary.PaymentResponse;
import org.sebjef.easypay.boundary.PaymentRequest;
import org.sebjef.easypay.entity.CardType;
import java.util.List;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.sebjef.easypay.entity.Payment;
import org.sebjef.easypay.entity.ProcessingMode;

/**
 *
 * @author JF James
 */
@QuarkusTest
public class TestPaymentService {

    @Inject
    PaymentService paymentService;

    @Inject
    @ConfigProperty(name = "payment.author.threshold", defaultValue = "10000")
    int authorThreshold;

    @Inject
    @ConfigProperty(name = "payment.max.amount.fallback", defaultValue = "20000")
    int maxAmountFallBack;


    @Test
    public void testConfig() {
        Assertions.assertTrue(authorThreshold == 10000);
        Assertions.assertTrue(maxAmountFallBack == 20000);
    }

    @Test
    public void testVisaAcceptedPayment() {
        PaymentRequest request = new PaymentRequest();
        request.setAmount(authorThreshold - 1);
        request.setCardNumber("4716727468113894");
        request.setExpiryDate("01/20");
        request.setPosId("POS-01");

        PaymentProcessingContext context = new PaymentProcessingContext(request);
        paymentService.accept(context);
        PaymentResponse response = context.generateResponse();

        Assertions.assertTrue(response.getResponseCode().equals(PaymentResponseCode.ACCEPTED));
        Assertions.assertTrue(response.getCardType().equals(CardType.VISA));
    }

    @Test
    public void testVisaRejectedPayment() {
        PaymentRequest request = new PaymentRequest();
        request.setAmount(authorThreshold - 1);
        request.setCardNumber("4485248221242242");
        request.setExpiryDate("01/20");
        request.setPosId("POS-01");

        PaymentProcessingContext context = new PaymentProcessingContext(request);
        paymentService.accept(context);
        PaymentResponse response = context.generateResponse();

        Assertions.assertTrue(response.getResponseCode().equals(PaymentResponseCode.BLACK_LISTED_CARD_NUMBER));
        Assertions.assertTrue(response.getCardType().equals(CardType.VISA));
    }

    @Test
    public void testDinersAcceptedPayment() {
        PaymentRequest request = new PaymentRequest();
        request.setAmount(authorThreshold - 1);
        request.setCardNumber("30250213149136");
        request.setExpiryDate("01/20");
        request.setPosId("POS-01");

        PaymentProcessingContext context = new PaymentProcessingContext(request);
        paymentService.accept(context);
        PaymentResponse response = context.generateResponse();

        Assertions.assertTrue(response.getResponseCode().equals(PaymentResponseCode.ACCEPTED));
        Assertions.assertTrue(response.getCardType().equals(CardType.DINERS_CLUB));
    }

    // @Test
    public void testBankAuthor() {
        PaymentRequest request = new PaymentRequest();
        request.setAmount(authorThreshold + 1);
        request.setCardNumber("30250213149136");
        request.setExpiryDate("01/20");
        request.setPosId("POS-01");

        PaymentProcessingContext context = new PaymentProcessingContext(request);
        paymentService.accept(context);
        PaymentResponse response = context.generateResponse();

        Assertions.assertTrue(response.getCardType().equals(CardType.DINERS_CLUB));

        // Should be authorized or fallback
        ProcessingMode processingMode = response.getProcessingMode();
        Assertions.assertTrue(processingMode.equals(ProcessingMode.STANDARD) || processingMode.equals(ProcessingMode.FALLBACK));

    }

    @Test
    public void testFindAndCount() {

        List<Payment> payments = paymentService.findAll();
        if (payments.isEmpty()) {
            return;
        }

        Assertions.assertTrue(paymentService.count() == payments.size());

        Payment firstPayment = payments.get(0);

        Assertions.assertNotNull(paymentService.findById(firstPayment.getId()));

    }

}