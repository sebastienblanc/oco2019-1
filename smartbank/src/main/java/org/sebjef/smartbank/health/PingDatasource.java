/*
 * Copyright 2019 jefrajames.
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
package org.sebjef.smartbank.health;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 *
 * @author jefrajames
 */
@Singleton
public class PingDatasource {

    @PersistenceContext(unitName = "smartbank")
    private EntityManager em;
    
    @Inject
    @ConfigProperty(name = "datasource.probe.request", defaultValue="SELECT 1 FROM DUAL")
    private String databaseRequest;

    public boolean doIt() {
        try {
            em.createNativeQuery("select 1 from INFORMATION_SCHEMA.SYSTEM_USERS").getSingleResult();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
