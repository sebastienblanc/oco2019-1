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
package org.sebjef.easypay.control;


import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import static javax.transaction.Transactional.TxType.REQUIRED;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.sebjef.easypay.control.bank.BankAuthorService;
import org.sebjef.easypay.entity.Payment;
import org.sebjef.easypay.entity.ProcessingMode;
import org.sebjef.easypay.entity.PaymentResponseCode;
import org.sebjef.easypay.entity.CardType;

/**
 *
 * @author JF James
 */
@ApplicationScoped
public class PaymentService {


    @Inject
    CardValidator cardValidator;

    @Inject
    PosValidator posValidator;

    @Inject
    BankAuthorService bankAuthorService;

    @Inject
    @ConfigProperty(name = "payment.author.threshold", defaultValue = "10000")
    int authorThreshold;


    private void process(PaymentProcessingContext context) {        

        if (!posValidator.isActive(context.getPosId())) {
            context.setResponseCode(PaymentResponseCode.INACTIVE_POS);
            return;
        }

        if (!cardValidator.checkCardNumber(context.getCardNumber())) {
            context.setResponseCode(PaymentResponseCode.INVALID_CARD_NUMBER);
            return;
        }

        CardType cardType = cardValidator.checkCardType(context.getCardNumber());
        if (cardType == CardType.UNKNOWN) {
            context.setResponseCode(PaymentResponseCode.UNKNWON_CARD_TYPE);
            return;
        }
        context.setCardType(cardType);

        if (cardValidator.isBlackListed(context.getCardNumber())) {
            context.setResponseCode(PaymentResponseCode.BLACK_LISTED_CARD_NUMBER);
            return;
        }

        if (context.getAmount() > authorThreshold) {
            if (!bankAuthorService.authorize(context)) {
                // Authorization refused: locally (AMOUNT_EXCEEDED) or remotely by the Bank (AUTHORIZATION_DENIED)?
                context.setResponseCode(context.getProcessingMode().equals(ProcessingMode.STANDARD) ? PaymentResponseCode.AUTHORIZATION_DENIED : PaymentResponseCode.AMOUNT_EXCEEDED);
            }
        }

    }


    private void store(PaymentProcessingContext context) {
        Payment payment = new Payment();

        payment.setAmount(context.getAmount());
        payment.setCardNumber(context.getCardNumber());
        payment.setExpiryDate(context.getExpiryDate());
        payment.setResponseCode(context.getResponseCode());
        payment.setProcessingMode(context.getProcessingMode());
        payment.setCardType(context.getCardType());
        payment.setPosId(context.getPosId());
        payment.setDateTime(context.getDateTime());
        context.setResponseTime(System.currentTimeMillis() - context.getResponseTime());
        payment.setReponseTime(context.getResponseTime());
        payment.setBankCalled(context.isBankCalled());
        payment.setAuthorized(context.isAuthorized());
        if (context.getAuthorId().isPresent()) {
            payment.setAuthorId(context.getAuthorId().get());
        }

        Payment.persist(payment);

        context.setId(payment.getId());
    }

    // @Transactional is only effective on externally called methods with Parayra 5.191
    @Transactional(REQUIRED)
    public void accept(PaymentProcessingContext paymentContext) {
        process(paymentContext);
        store(paymentContext);
    }

    public Payment findById(long id) {
        return Payment.findById(id);
    }

    public List<Payment> findAll() {
        return Payment.listAll();
    }

    public long count() {
        return Payment.count();
    }
}
