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
import org.junit.jupiter.api.Test;
import org.sebjef.easypay.control.CardValidator;
import org.sebjef.easypay.entity.CardType;
import javax.inject.Inject;
import org.junit.jupiter.api.Assertions;


/**
 * 
 * 
 * @author JF James
 */

@QuarkusTest
public class TestCardValidator {

    @Inject
    private CardValidator cardValidator;



    @Test
    public void testExpiryDate() {
        Assertions. assertFalse(cardValidator.checkExpiryDate("02/19"));
        Assertions.assertTrue(cardValidator.checkExpiryDate("04/20"));
        Assertions.assertTrue(cardValidator.checkExpiryDate("04/21"));
    }

    @Test
    public void testCardNumber() {

        Assertions.assertTrue(cardValidator.checkCardNumber("4024007139653132"));
        Assertions.assertTrue(cardValidator.checkCardNumber("5241089798053246"));
        Assertions.assertTrue(cardValidator.checkCardNumber("6011072800365366"));
        Assertions.assertTrue(cardValidator.checkCardNumber("349157384841589"));

        Assertions.assertFalse(cardValidator.checkCardNumber("349157384841581"));
        Assertions.assertFalse(cardValidator.checkCardNumber("abcd"));
        Assertions.assertFalse(cardValidator.checkCardNumber("34915t384841589"));

    }

    @Test
    public void testCardType() {
        Assertions.assertEquals(CardType.VISA, cardValidator.checkCardType("4000056655665556"));
        Assertions.assertEquals(CardType.VISA, cardValidator.checkCardType("4242424242424242"));

        Assertions.assertEquals(CardType.MASTERCARD, cardValidator.checkCardType("5105105105105100"));
        Assertions.assertEquals(CardType.MASTERCARD, cardValidator.checkCardType("5200828282828210"));
        Assertions.assertEquals(CardType.MASTERCARD, cardValidator.checkCardType("5555555555554444"));

        Assertions.assertEquals(CardType.AMERICAN_EXPRESS, cardValidator.checkCardType("371449635398431"));
        Assertions.assertEquals(CardType.AMERICAN_EXPRESS, cardValidator.checkCardType("378282246310005"));

        Assertions.assertEquals(CardType.DISCOVER, cardValidator.checkCardType("6011000990139424"));
        Assertions.assertEquals(CardType.DISCOVER, cardValidator.checkCardType("6011111111111117"));

        Assertions.assertEquals(CardType.DINERS_CLUB, cardValidator.checkCardType("30569309025904"));
        Assertions.assertEquals(CardType.DINERS_CLUB, cardValidator.checkCardType("38520000023237"));

        Assertions.assertEquals(CardType.JCB, cardValidator.checkCardType("3530111333300000"));
        Assertions.assertEquals(CardType.JCB, cardValidator.checkCardType("3566002020360505"));

        Assertions.assertEquals(CardType.UNKNOWN, cardValidator.checkCardType("0000000000000000"));
    }
    
    @Test
    public void testBlackListed() {
        Assertions.assertTrue(cardValidator.isBlackListed("4485248221242242"));
        Assertions.assertFalse(cardValidator.isBlackListed("4716727468113894"));

        Assertions.assertTrue(cardValidator.isBlackListed("6011191990123805"));
        Assertions.assertFalse(cardValidator.isBlackListed("6011716535549278"));
    }

}