/**
 * ====
 *     pingchecker - Tool to periodically check services availability
 *     Copyright (c) 2015, Matej Kormuth <http://www.github.com/dobrakmato>
 *     All rights reserved.
 *
 *     Redistribution and use in source and binary forms, with or without modification,
 *     are permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this
 *     list of conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation and/or
 *     other materials provided with the distribution.
 *
 *     THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *     ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *     WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *     DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 *     ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *     (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *     LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *     ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *     (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *     SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * ====
 *
 * Pingchecker.eu webapp - Tool to periodically check services availability
 * Copyright (c) 2015, Matej Kormuth <http://www.github.com/dobrakmato>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
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
import eu.matejkormuth.pingchecker.beans.Category;
import eu.matejkormuth.pingchecker.beans.Target;
import eu.matejkormuth.pingchecker.beans.User;
import eu.matejkormuth.pingchecker.webapp.SessionAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class TargetsController {

    @RequestMapping("/targets")
    public ModelAndView viewTargets(HttpSession session) {
        User user = (User) session.getAttribute(SessionAttributes.USER_KEY);
        List<Category> categories = Ebean.createQuery(Category.class)
                .select("id, name")
                .fetch("targets", "id, name")
                .where()
                .eq("t0.owner_id", user.getId())
                .query()
                .findList();

        ModelAndView modelAndView = new ModelAndView("admin/targets");
        modelAndView.addObject("categories", categories);
        return modelAndView;
    }

    @RequestMapping("/targets/add")
    public ModelAndView showAddTarget() {
        ModelAndView modelAndView = new ModelAndView("admin/add_target");
        return modelAndView;
    }

    @RequestMapping(value = "/targets/add", method = RequestMethod.POST)
    public Object showAddTarget(@RequestParam("name") String name,
                                @RequestParam("address") String address,
                                @RequestParam("check_type") String checkType,
                                @RequestParam("category") String category,
                                @RequestParam("check_interval") int checkInterval,
                                HttpSession session) {
        // Check for validity of category, checkType, checkInterval, name, address
        String errors = "";

        // Check for invalid values.
        if (name.isEmpty()) {
            errors += "Name can't be empty!<br>";
        }

        if (name.length() > 255) {
            errors += "Name must be shorter than 255 characters!<br>";
        }

        if (address.isEmpty()) {
            errors += "Target address can't be empty!<br>";
        }

        if (address.length() > 255) {
            errors += "Address must be shorter than 255 characters!<br>";
        }

        try {
            InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            errors += "Specified target address is not valid IP address or hostname!<br>";
        }

        if (checkType.isEmpty()) {
            errors += "Check type can't be empty!<br>";
        }

        try {
            Target.Type.valueOf(checkType);
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

        Category category1 = Ebean.find(Category.class, category);

        if(category1 == null) {
            errors += "Invalid category!<br>";
        }

        // If there were errors, display them.
        if(!errors.isEmpty()) {
            ModelAndView modelAndView = new ModelAndView("admin/add_target");
            modelAndView.addObject("errors", errors);
            modelAndView.addObject("address", address);
            modelAndView.addObject("name", name);
            modelAndView.addObject("checkType", checkType);
            modelAndView.addObject("checkInterval", checkInterval);
            modelAndView.addObject("category", category);
            return modelAndView;
        }

        // Create new object.
        Target target = new Target();
        target.setAddress(address);
        target.setCategory(category1);
        target.setCheckInterval(checkInterval);
        target.setName(name);
        target.setOwner((User) session.getAttribute(SessionAttributes.USER_KEY));
        target.setType(Target.Type.valueOf(checkType));

        Ebean.save(target);

        return new RedirectView("/admin/target/" + target.getId());
    }
}
