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
import java.util.logging.Level;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.java.Log;
import org.sebjef.easypay.entity.PosRef;

/**
 *
 * @author JF James
 */
@ApplicationScoped
@Log
public class PosValidator {

    @PersistenceContext(unitName = "easypay")
    private EntityManager em;

    public boolean isActive(String posId) {

        List<PosRef> posList = em.createNamedQuery("PosRef.findByPosId", PosRef.class).setParameter("posId", posId).getResultList();
        if (posList.isEmpty()) {
            log.log(Level.WARNING, "checkPosStatus NOK, unknown posId {0}", posId);
            return false;
        }

        boolean result = posList.get(0).isActive();

        if (!result) {
            log.log(Level.WARNING, "checkPosStatus NOK, inactive posId {0}", posId);
        }

        return result;
    }

}
