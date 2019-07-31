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

import org.sebjef.easypay.entity.PaymentResponseCode;
import org.sebjef.easypay.control.PaymentProcessingContext;
import org.sebjef.easypay.control.PaymentService;
import org.sebjef.easypay.boundary.PaymentResponse;
import org.sebjef.easypay.boundary.PaymentRequest;
import org.sebjef.easypay.entity.CardType;
import java.io.File;
import java.util.List;
import javax.inject.Inject;
import lombok.extern.java.Log;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sebjef.easypay.config.DataSourceConfiguration;
import org.sebjef.easypay.control.bank.BankAuthorService;
import org.sebjef.easypay.entity.Payment;
import org.sebjef.easypay.entity.ProcessingMode;

/**
 *
 * @author JF James
 */
@RunWith(Arquillian.class)
@Log
public class TestPaymentService {

    @Inject
    PaymentService paymentService;

    @Inject
    @ConfigProperty(name = "payment.author.threshold", defaultValue = "10000")
    int authorThreshold;

    @Inject
    @ConfigProperty(name = "payment.max.amount.fallback", defaultValue = "20000")
    int maxAmountFallBack;

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class);

        // Non-Java EE & MP libraries
        File[] libs = Maven.resolver().loadPomFromFile("pom.xml").importRuntimeDependencies().resolve().withTransitivity().asFile();
        archive.addAsLibraries(libs);

        // App. classes
        archive.addPackages(false, Filters.exclude(".*Test.*"), PaymentRequest.class.getPackage());
        archive.addPackages(false, Filters.exclude(".*Test.*"), PaymentService.class.getPackage());
        archive.addPackages(false, Filters.exclude(".*Test.*"), BankAuthorService.class.getPackage());
        archive.addPackages(false, Filters.exclude(".*Test.*"), Payment.class.getPackage());
        archive.addClass(DataSourceConfiguration.class);

        // App. config
        archive.addAsWebInfResource("META-INF/beans.xml", "beans.xml");
        archive.addAsResource("META-INF/persistence.xml");
        archive.addAsResource("META-INF/load.sql");
        archive.addAsResource("META-INF/microprofile-config.properties");

        log.log(java.util.logging.Level.INFO, "deploying webarchive: {0}", archive.toString(true));

        return archive;
    }

    @Test
    public void testConfig() {
        assertTrue(authorThreshold == 10000);
        assertTrue(maxAmountFallBack == 20000);
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

        assertTrue(response.getResponseCode().equals(PaymentResponseCode.ACCEPTED));
        assertTrue(response.getCardType().equals(CardType.VISA));
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

        assertTrue(response.getResponseCode().equals(PaymentResponseCode.BLACK_LISTED_CARD_NUMBER));
        assertTrue(response.getCardType().equals(CardType.VISA));
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

        assertTrue(response.getResponseCode().equals(PaymentResponseCode.ACCEPTED));
        assertTrue(response.getCardType().equals(CardType.DINERS_CLUB));
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

        assertTrue(response.getCardType().equals(CardType.DINERS_CLUB));

        // Should be authorized or fallback
        ProcessingMode processingMode = response.getProcessingMode();
        log.info("response=" + response);
        assertTrue(processingMode.equals(ProcessingMode.STANDARD) || processingMode.equals(ProcessingMode.FALLBACK));

    }

    @Test
    public void testFindAndCount() {

        List<Payment> payments = paymentService.findAll();
        if (payments.isEmpty()) {
            return;
        }

        assertTrue(paymentService.count() == payments.size());

        Payment firstPayment = payments.get(0);

        assertNotNull(paymentService.findById(firstPayment.getId()));

    }

}
