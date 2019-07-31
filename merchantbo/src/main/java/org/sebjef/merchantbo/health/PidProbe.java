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
package org.sebjef.merchantbo.health;

import java.util.logging.Level;
import javax.enterprise.context.ApplicationScoped;
import lombok.extern.java.Log;
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;

/**
 *
 * @author jefrajames
 */
@ApplicationScoped
@Health
@Log
public class PidProbe implements HealthCheck {

    @Override
    public HealthCheckResponse call() {

        long pid = -1;

        try {
            pid = ProcessHandle.current().pid();
        } catch (NoClassDefFoundError ex) {
            // NoClasdDefFoundError if Java 8
            log.log(Level.WARNING, "PidProbe NOK {0}", ex.getMessage());
        }

        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("pid-probe")
                .withData("pid", pid);

        return responseBuilder.up().build();
    }

}
