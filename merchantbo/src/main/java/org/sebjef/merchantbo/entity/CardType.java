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
package org.sebjef.merchantbo.entity;

import java.util.logging.Level;
import java.util.regex.Pattern;
import lombok.extern.java.Log;

/**
 * From: 
 * https://gist.github.com/gabrielbauman/f3db7ce8ae7828fa05b3
 * https://hzhou.me/2014/08/13/java-get-credit-card-type-by-its-number/
 * 
 * @author JF James
 */
@Log
public enum CardType {

    UNKNOWN,
    VISA("^4[0-9]{12}(?:[0-9]{3}){0,2}$"), // 13 or 16 digits, starting with 4
    MASTERCARD("^(?:5[1-5]|2(?!2([01]|20)|7(2[1-9]|3))[2-7])\\d{14}$"), // 16 digits, starting with 51 through 55.
    AMERICAN_EXPRESS("^3[47][0-9]{13}$"), // 15 digits, starting with 34 or 37
    DINERS_CLUB("^3(?:0[0-5]|[68][0-9])[0-9]{11}$"), // 14 digits, starting with 300 through 305, 36, or 38
    DISCOVER("^6(?:011|[45][0-9]{2})[0-9]{12}$"), // 16 digits, starting with 6011 or 65
    JCB("^(?:2131|1800|35\\d{3})\\d{11}$"), // 15 digits, starting with 2131 or 1800, or 16 digits starting with 35
    CHINA_UNION_PAY("^62[0-9]{14,17}$"); // start with 62 and is 16-19 digit long

    private Pattern pattern;

    CardType() {
        this.pattern = null;
    }

    CardType(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    public static CardType detect(String cardNumber) {

        for (CardType cardType : CardType.values()) {
            if (null == cardType.pattern) {
                continue;
            }
            if (cardType.pattern.matcher(cardNumber).matches()) {
                return cardType;
            }
        }

        log.log(Level.WARNING, "card number {0} has an unknown card type", cardNumber);
        return UNKNOWN;
    }

}
