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

import org.sebjef.easypay.control.CardValidator;
import org.sebjef.easypay.entity.CardType;
import java.io.File;
import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import lombok.extern.java.Log;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.sebjef.easypay.config.DataSourceConfiguration;
import org.sebjef.easypay.entity.CardRef;


/**
 * 
 * 
 * @author JF James
 */
@RunWith(Arquillian.class)
@Log
public class TestCardValidator {

    @Inject
    private CardValidator cardValidator;

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class);

        // Non-Java EE & MP libraries
        File[] libs = Maven.resolver().loadPomFromFile("pom.xml").importRuntimeDependencies().resolve().withTransitivity().asFile();
        archive.addAsLibraries(libs);
        
        // App. classes
        archive.addPackages(false, Filters.exclude(".*Test.*"), CardValidator.class.getPackage());
        archive.addPackages(false, Filters.exclude(".*Test.*"), CardRef.class.getPackage());
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
    public void testExpiryDate() {
        assertFalse(cardValidator.checkExpiryDate("02/19"));
        assertTrue(cardValidator.checkExpiryDate("04/20"));
        assertTrue(cardValidator.checkExpiryDate("04/21"));
    }

    @Test
    public void testCardNumber() {

        assertTrue(cardValidator.checkCardNumber("4024007139653132"));
        assertTrue(cardValidator.checkCardNumber("5241089798053246"));
        assertTrue(cardValidator.checkCardNumber("6011072800365366"));
        assertTrue(cardValidator.checkCardNumber("349157384841589"));

        assertFalse(cardValidator.checkCardNumber("349157384841581"));
        assertFalse(cardValidator.checkCardNumber("abcd"));
        assertFalse(cardValidator.checkCardNumber("34915t384841589"));

    }

    @Test
    public void testCardType() {
        assertEquals(CardType.VISA, cardValidator.checkCardType("4000056655665556"));
        assertEquals(CardType.VISA, cardValidator.checkCardType("4242424242424242"));

        assertEquals(CardType.MASTERCARD, cardValidator.checkCardType("5105105105105100"));
        assertEquals(CardType.MASTERCARD, cardValidator.checkCardType("5200828282828210"));
        assertEquals(CardType.MASTERCARD, cardValidator.checkCardType("5555555555554444"));

        assertEquals(CardType.AMERICAN_EXPRESS, cardValidator.checkCardType("371449635398431"));
        assertEquals(CardType.AMERICAN_EXPRESS, cardValidator.checkCardType("378282246310005"));

        assertEquals(CardType.DISCOVER, cardValidator.checkCardType("6011000990139424"));
        assertEquals(CardType.DISCOVER, cardValidator.checkCardType("6011111111111117"));

        assertEquals(CardType.DINERS_CLUB, cardValidator.checkCardType("30569309025904"));
        assertEquals(CardType.DINERS_CLUB, cardValidator.checkCardType("38520000023237"));

        assertEquals(CardType.JCB, cardValidator.checkCardType("3530111333300000"));
        assertEquals(CardType.JCB, cardValidator.checkCardType("3566002020360505"));

        assertEquals(CardType.UNKNOWN, cardValidator.checkCardType("0000000000000000"));
    }
    
    @Test
    public void testBlackListed() {
        assertTrue(cardValidator.isBlackListed("4485248221242242"));
        assertFalse(cardValidator.isBlackListed("4716727468113894"));
        
        assertTrue(cardValidator.isBlackListed("6011191990123805"));
        assertFalse(cardValidator.isBlackListed("6011716535549278"));
    }

}