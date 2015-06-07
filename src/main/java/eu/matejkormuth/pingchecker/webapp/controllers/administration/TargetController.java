/**
 * ====
 * pingchecker - Tool to periodically check services availability
 * Copyright (c) 2015, Matej Kormuth <http://www.github.com/dobrakmato>
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * <p>
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * <p>
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * ====
 * <p>
 * Pingchecker.eu webapp - Tool to periodically check services availability
 * Copyright (c) 2015, Matej Kormuth <http://www.github.com/dobrakmato>
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * <p>
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * <p>
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package eu.matejkormuth.pingchecker.webapp.controllers.administration;

import com.avaje.ebean.Ebean;
import eu.matejkormuth.pingchecker.beans.Ping;
import eu.matejkormuth.pingchecker.beans.Target;
import eu.matejkormuth.pingchecker.webapp.Reversed;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class TargetController {

    @RequestMapping("/target/{id}")
    public ModelAndView viewDetails(@PathVariable("id") int id) {

        Target target = Ebean.createQuery(Target.class)
                .select("name, address, type, checkInterval")
                .fetch("category", "name")
                .where()
                .eq("id", id)
                .query()
                .findUnique();

        List<Ping> pingList = Ebean.createQuery(Ping.class)
                .where()
                .eq("target_id", id)
                .ge("timestamp", new Timestamp(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000))
                .query()
                .orderBy()
                .desc("timestamp")
                .findList();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM");
        StringBuilder labels = new StringBuilder();
        StringBuilder data = new StringBuilder();

        // Build json with reversed order (ascending).
        for (Ping ping : new Reversed<>(pingList)) {
            if (data.length() > 0) {
                data.append(", ");
                labels.append("\", \"");
            }
            data.append(ping.getPing());
            labels.append(simpleDateFormat.format(ping.getTimestamp()));
        }

        Ping ping = null;
        if (pingList.size() > 0) {
            ping = pingList.get(0);
        }

        ModelAndView modelAndView = new ModelAndView("admin/target_details");
        modelAndView.addObject("service", target);
        modelAndView.addObject("lastPing", ping);
        modelAndView.addObject("checkTypes", Target.Type.values());
        modelAndView.addObject("graphLabels", "\"" + labels.toString() + "\"");
        modelAndView.addObject("graphData", data.toString());

        return modelAndView;
    }

    @RequestMapping(value = "/target/{id}", method = RequestMethod.POST)
    public Object updateDetails(@PathVariable("id") int id,
                                @RequestParam("service_address") String serviceAddress,
                                @RequestParam("service_name") String serviceName,
                                @RequestParam("check_type") String serviceType,
                                @RequestParam("check_interval") int checkInterval) {

        String errors = "";

        // Check for invalid values.
        if (serviceAddress.isEmpty()) {
            errors += "Target address can't be empty!<br>";
        }

        if (serviceName.isEmpty()) {
            errors += "Name can't be empty!<br>";
        }

        try {
            if(serviceAddress.contains(":")) {
                InetAddress.getByName(serviceAddress.split(":")[0]);
            } else {
                InetAddress.getByName(serviceAddress);
            }
        } catch (UnknownHostException e) {
            errors += "Specified target address is not valid IP address or hostname!<br>";
        }

        if (serviceType.isEmpty()) {
            errors += "Check type can't be empty!<br>";
        }

        try {
            Target.Type.valueOf(serviceType);
        } catch (IllegalArgumentException e) {
            StringBuilder validTypes = new StringBuilder();
            for (Target.Type t : Target.Type.values()) {
                if (validTypes.length() > 0) {
                    validTypes.append(", ");
                }
                validTypes.append(t.name());
            }

            errors += "Invalid check type! Valid types are: " + validTypes.toString() + "<br>";
        }

        if (checkInterval < 15) {
            errors += "Check interval must be grater then 15 minutes!<br>";
        }

        // Get pings from last seven days.
        List<Ping> pingList = Ebean.createQuery(Ping.class)
                .where()
                .eq("target_id", id)
                .ge("timestamp", new Timestamp(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000))
                .query()
                .orderBy()
                .desc("timestamp")
                .findList();

        Ping ping = null;
        if (pingList.size() >= 1) {
            ping = pingList.get(0);
        }


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM");
        StringBuilder labels = new StringBuilder();
        StringBuilder data = new StringBuilder();

        // Build json with reversed order (ascending).
        for (Ping ping2 : new Reversed<>(pingList)) {
            if (data.length() > 0) {
                data.append(", ");
                labels.append("\", \"");
            }
            data.append(ping2.getPing());
            labels.append(simpleDateFormat.format(ping2.getTimestamp()));
        }

        if (errors.isEmpty()) {
            // Perform an update.
            Target target = Ebean.createQuery(Target.class)
                    .where()
                    .eq("id", id)
                    .query()
                    .findUnique();

            target.setAddress(serviceAddress);
            target.setName(serviceName);
            target.setType(Target.Type.valueOf(serviceType));
            target.setCheckInterval(checkInterval);
            Ebean.update(target);

            ModelAndView modelAndView = new ModelAndView("admin/target_details");
            modelAndView.addObject("service", target);
            modelAndView.addObject("lastPing", ping);
            modelAndView.addObject("checkTypes", Target.Type.values());
            modelAndView.addObject("graphLabels", "\"" + labels.toString() + "\"");
            modelAndView.addObject("graphData", data.toString());
            modelAndView.addObject("success", "Configuration updated!");
            return modelAndView;

        } else {
            // Display an error page.
            Target target = Ebean.createQuery(Target.class)
                    .select("name, address, type, checkInterval")
                    .fetch("category", "name")
                    .where()
                    .eq("id", id)
                    .query()
                    .findUnique();

            ModelAndView modelAndView = new ModelAndView("admin/target_details");
            modelAndView.addObject("service", target);
            modelAndView.addObject("lastPing", ping);
            modelAndView.addObject("checkTypes", Target.Type.values());
            modelAndView.addObject("graphLabels", "\"" + labels.toString() + "\"");
            modelAndView.addObject("graphData", data.toString());
            modelAndView.addObject("error", errors);
            return modelAndView;
        }
    }
}
