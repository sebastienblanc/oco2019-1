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
package org.sebjef.easypay.control.bank;

import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.sebjef.easypay.control.PaymentProcessingContext;
import org.sebjef.easypay.entity.ProcessingMode;

/**
 *
 * @author JF James
 */
@ApplicationScoped
public class BankAuthorService {

    @Inject
    @ConfigProperty(name = "payment.author.merchantId", defaultValue = "Devoxx Store")
    String merchantId;

    @Inject
    @ConfigProperty(name = "payment.max.amount.fallback", defaultValue = "20000")
    int maxAmountFallBack;

    @Inject
    @RestClient
    BankAuthorClient bankAuthorClient;

    private BankAuthorRequest initRequest(PaymentProcessingContext context) {
        BankAuthorRequest request = new BankAuthorRequest();
        request.setMerchantId(merchantId);
        request.setAmount(context.getAmount());
        request.setCardNumber(context.getCardNumber());
        request.setExpiryDate(context.getExpiryDate());
        return request;
    }

    @Retry(maxRetries = 1, delay = 200, maxDuration = 1000)
    @Fallback(fallbackMethod = "acceptByDelegation")
    public boolean authorize(PaymentProcessingContext context) {
        
        try {
            BankAuthorResponse response = bankAuthorClient.authorize(initRequest(context));
            context.setBankCalled(true);
            context.setAuthorId(Optional.of(response.getAuthorId()));
            context.setAuthorized(response.isAuthorized());
            return context.isAuthorized();
        } catch (RuntimeException e) {
            // If empty endpoint ProcessingException: Error writing JSON-B serialized object
            //log.log(Level.WARNING, "BankAuthorService.authorize NOK {0}", e);
            throw e;
        }
    }

    // Fallback method must be public: with Payara @Timeout or @Retry needed to call it properly
    public boolean acceptByDelegation(PaymentProcessingContext context) {
        context.setBankCalled(false);
        context.setProcessingMode(ProcessingMode.FALLBACK);
        return (context.getAmount() <= maxAmountFallBack);
    }

}
