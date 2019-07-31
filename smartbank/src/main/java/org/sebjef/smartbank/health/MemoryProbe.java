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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.text.NumberFormat;
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
@Health
@ApplicationScoped
@Log
public class MemoryProbe implements HealthCheck {

    @Override
    public HealthCheckResponse call() {

        long rss = -1;

        try {
            long pid = ProcessHandle.current().pid();
            ProcessBuilder pb = new ProcessBuilder();
            pb.command("bash", "-c", "ps -o rss -p " + pid + " | tail -1");
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String rssAsString = reader.readLine();
            rss = Long.parseLong(rssAsString.trim());
        } catch (IOException | NumberFormatException | NoClassDefFoundError ex) {
            // NoClasdDefFoundError if Java 8
            log.log(Level.WARNING, "MemoryProbe NOK {0}", ex.getMessage());
        }

        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();

        NumberFormat formatter = NumberFormat.getInstance(java.util.Locale.US);

        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("memory-probe")
                .withData("rss", (rss == -1) ? "-1" : formatter.format(rss))
                .withData("non-heap", formatter.format(memoryBean.getNonHeapMemoryUsage().getUsed() / 1024))
                .withData("heap", formatter.format(memoryBean.getHeapMemoryUsage().getUsed() / 1024));

        return responseBuilder.up().build();

    }

}
