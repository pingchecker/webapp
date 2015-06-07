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
package eu.matejkormuth.pingchecker.webapp.controllers;

import com.avaje.ebean.Ebean;
import eu.matejkormuth.pingchecker.beans.User;
import eu.matejkormuth.pingchecker.webapp.SessionAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @RequestMapping("/login")
    public ModelAndView login(@RequestParam(value = "redirect", defaultValue = "") String redirect) {
        if (redirect != null && !redirect.isEmpty()) {
            ModelAndView modelAndView = new ModelAndView("login");
            modelAndView.addObject("error", "You have to authenticated to view that page!");
            return modelAndView;
        }

        return new ModelAndView("login");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object doLogin(@RequestParam(value = "redirect", defaultValue = "") String redirect,
                          @RequestParam("email") String email,
                          @RequestParam("password") String password,
                          HttpSession session) throws ServletException {
        User user = Ebean.createQuery(User.class)
                .where()
                .eq("email", email)
                .query()
                .findUnique();

        if (user == null) {
            ModelAndView modelAndView = new ModelAndView("login");
            modelAndView.addObject("error", "Invalid email address.");
            return modelAndView;
        }

        // Check for password match.
        if (password.equals(user.getPassword())) {
            session.setAttribute(SessionAttributes.USER_KEY, user);

            if (redirect != null && !redirect.isEmpty()) {
                return new RedirectView(redirect);
            } else {
                // Redirect to target view.
                return new RedirectView("/admin/targets");
            }
        } else {
            ModelAndView modelAndView = new ModelAndView("login");
            modelAndView.addObject("error", "Invalid password.");
            return modelAndView;
        }
    }

    @RequestMapping("/logout")
    public RedirectView logout(HttpSession session) {
        // Set current user to null.
        session.setAttribute(SessionAttributes.USER_KEY, null);

        // Redirect to index.
        return new RedirectView("/");
    }
}
