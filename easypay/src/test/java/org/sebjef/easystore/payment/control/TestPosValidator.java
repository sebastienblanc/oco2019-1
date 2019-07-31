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

import org.sebjef.easypay.control.PosValidator;
import java.io.File;
import javax.inject.Inject;
import lombok.extern.java.Log;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sebjef.easypay.config.DataSourceConfiguration;
import org.sebjef.easypay.entity.PosRef;

/**
 *
 * @author JF James
 */
@RunWith(Arquillian.class)
@Log
public class TestPosValidator {

    @Inject
    PosValidator posValidator;

    @Deployment
    public static WebArchive deploy() {

        WebArchive archive = ShrinkWrap.create(WebArchive.class);

        // Non-Java EE & MP libraries
        File[] libs = Maven.resolver().loadPomFromFile("pom.xml").importRuntimeDependencies().resolve().withTransitivity().asFile();
        archive.addAsLibraries(libs);

        // App. classes
        archive.addPackages(false, Filters.exclude(".*Test.*"), PosValidator.class.getPackage());
        archive.addPackages(false, Filters.exclude(".*Test.*"), PosRef.class.getPackage());
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
    public void testActivePos() throws Exception {
        assertTrue(posValidator.isActive("POS-01"));
    }

    @Test
    public void testUnknownPos() {
        assertFalse(posValidator.isActive("POS-99"));
    }

    @Test
    public void testInactivePos() {
        assertFalse(posValidator.isActive("POS-03"));
    }

}
