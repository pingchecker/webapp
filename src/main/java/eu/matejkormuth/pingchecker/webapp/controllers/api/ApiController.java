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
package eu.matejkormuth.pingchecker.webapp.controllers.api;

import com.avaje.ebean.Ebean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import eu.matejkormuth.pingchecker.beans.Category;
import eu.matejkormuth.pingchecker.beans.Ping;
import eu.matejkormuth.pingchecker.beans.Target;
import eu.matejkormuth.pingchecker.beans.User;
import eu.matejkormuth.pingchecker.webapp.EntityLinkTypeAdapterFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ApiController {

    EntityLinkTypeAdapterFactory factory = new EntityLinkTypeAdapterFactory();
    Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(User.class, factory.create(null, TypeToken.get(User.class)))
            .registerTypeHierarchyAdapter(Target.class, factory.create(null, TypeToken.get(Target.class)))
            .registerTypeHierarchyAdapter(Category.class, factory.create(null, TypeToken.get(Category.class)))
            .registerTypeHierarchyAdapter(Ping.class, factory.create(null, TypeToken.get(Ping.class)))
            .setPrettyPrinting()
            .create();

    @RequestMapping(value = "/target/{id}", produces = {"text/plain", "application/json"})
    @ResponseBody
    String getTarget(@PathVariable("id") int id) {
        Target target = Ebean.createQuery(Target.class)
                .where()
                .eq("id", id)
                .query()
                .findUnique();
        return gson.toJson(target);
    }

    @RequestMapping(value = "/target/{id}/pings", produces = {"text/plain", "application/json"})
    @ResponseBody
    String getTargetPings(@PathVariable("id") int id, @RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<Ping> target = Ebean.createQuery(Ping.class)
                .where()
                .eq("target_id", id)
                .order()
                .desc("timestamp")
                .setMaxRows(limit)
                .findList();
        return gson.toJson(target);
    }

    @RequestMapping(value = "/user/{id}", produces = {"text/plain", "application/json"})
    @ResponseBody
    String getUser(@PathVariable("id") int id) {
        User user = Ebean.createQuery(User.class)
                .where()
                .eq("id", id)
                .query()
                .findUnique();
        return gson.toJson(user);
    }

    @RequestMapping(value = "/user/{id}/targets", produces = {"text/plain", "application/json"})
    @ResponseBody
    String getUserTargets(@PathVariable("id") int id) {
        List<Target> targets = Ebean.createQuery(Target.class)
                .where()
                .eq("owner_id", id)
                .query()
                .findList();
        return gson.toJson(targets);
    }

    @RequestMapping(value = "/user/{id}/categories", produces = {"text/plain", "application/json"})
    @ResponseBody
    String getUserCategories(@PathVariable("id") int id) {
        List<Category> categories = Ebean.createQuery(Category.class)
                .where()
                .eq("owner_id", id)
                .query()
                .findList();
        return gson.toJson(categories);
    }

    @RequestMapping(value = "/ping/{id}", produces = {"text/plain", "application/json"})
    @ResponseBody
    String getPing(@PathVariable("id") int id) {
        Ping ping = Ebean.createQuery(Ping.class)
                .where()
                .eq("id", id)
                .query()
                .findUnique();
        return gson.toJson(ping);
    }

    @RequestMapping(value = "/category/{id}", produces = {"text/plain", "application/json"})
    @ResponseBody
    String getCategory(@PathVariable("id") int id) {
        Category category = Ebean.createQuery(Category.class)
                .where()
                .eq("id", id)
                .query()
                .findUnique();
        return gson.toJson(category);
    }

    @RequestMapping(value = "/category/{id}/targets", produces = {"text/plain", "application/json"})
    @ResponseBody
    String getCategoryTargets(@PathVariable("id") int id) {
        List<Target> targets = Ebean.createQuery(Target.class)
                .where()
                .eq("category_id", id)
                .query()
                .findList();
        return gson.toJson(targets);
    }
}
