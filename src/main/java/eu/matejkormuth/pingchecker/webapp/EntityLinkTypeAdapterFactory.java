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
package eu.matejkormuth.pingchecker.webapp;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import eu.matejkormuth.pingchecker.beans.Category;
import eu.matejkormuth.pingchecker.beans.Ping;
import eu.matejkormuth.pingchecker.beans.Target;
import eu.matejkormuth.pingchecker.beans.User;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

public class EntityLinkTypeAdapterFactory implements TypeAdapterFactory {

    private static final TypeToken userTypeToken;
    private static final TypeToken pingTypeToken;
    private static final TypeToken targetTypeToken;
    private static final TypeToken categoryTypeToken;

    static {
        userTypeToken = TypeToken.get(User.class);
        pingTypeToken = TypeToken.get(Ping.class);
        targetTypeToken = TypeToken.get(Target.class);
        categoryTypeToken = TypeToken.get(Category.class);
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (type.equals(pingTypeToken)) {
            return (TypeAdapter<T>) createPingAdapter();
        } else if (type.equals(targetTypeToken)) {
            return (TypeAdapter<T>) createTargetAdapter();
        } else if (type.equals(categoryTypeToken)) {
            return (TypeAdapter<T>) createCategoryAdapter();
        } else if (type.equals(userTypeToken)) {
            return (TypeAdapter<T>) createUserAdapter();
        } else {
            return null;
        }
    }

    private TypeAdapter<Ping> createPingAdapter() {
        return new TypeAdapter<Ping>() {
            @Override
            public void write(JsonWriter out, Ping value) throws IOException {
                out.beginObject();
                out.name("id").value(value.getId());
                out.name("target")
                        .beginObject()
                        .name("id").value(value.getTarget().getId())
                        .name("link").value(ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/v1/target/" + value.getTarget().getId()).build().toUriString())
                        .endObject();
                out.name("timestamp").value(value.getTimestamp().toString());
                out.name("latency").value(value.getPing());
                out.endObject();
            }

            @Override
            public Ping read(JsonReader in) throws IOException {
                return null;
            }
        };
    }

    private TypeAdapter<Target> createTargetAdapter() {
        return new TypeAdapter<Target>() {
            @Override
            public void write(JsonWriter out, Target value) throws IOException {
                out.beginObject();
                out.name("id").value(value.getId());
                out.name("type").value(value.getType().name());
                out.name("name").value(value.getName());
                out.name("address").value(value.getAddress());
                out.name("category")
                        .beginObject()
                        .name("id").value(value.getCategory().getId())
                        .name("link").value(ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/v1/category/" + value.getCategory().getId()).build().toUriString())
                        .endObject();
                out.name("owner")
                        .beginObject()
                        .name("id").value(value.getOwner().getId())
                        .name("link").value(ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/v1/user/" + value.getOwner().getId()).build().toUriString())
                        .endObject();
                out.name("check_interval").value(value.getCheckInterval());
                out.name("pings")
                        .beginObject()
                        .name("link").value(ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/v1/target/" + value.getId() + "/pings").build().toUriString())
                        .endObject();
                out.endObject();
            }

            @Override
            public Target read(JsonReader in) throws IOException {
                return null;
            }
        };
    }

    private TypeAdapter<Category> createCategoryAdapter() {
        return new TypeAdapter<Category>() {
            @Override
            public void write(JsonWriter out, Category value) throws IOException {
                out.beginObject();
                out.name("id").value(value.getId());
                out.name("name").value(value.getName());
                out.name("owner")
                        .beginObject()
                        .name("id").value(value.getOwner().getId())
                        .name("link").value(ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/v1/user/" + value.getOwner().getId()).build().toUriString())
                        .endObject();
                out.name("targets")
                        .beginObject()
                        .name("link").value(ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/v1/category/" + value.getId() + "/categories").build().toUriString())
                        .endObject();
                out.endObject();
            }

            @Override
            public Category read(JsonReader in) throws IOException {
                return null;
            }
        };
    }

    private TypeAdapter<User> createUserAdapter() {
        return new TypeAdapter<User>() {
            @Override
            public void write(JsonWriter out, User value) throws IOException {
                out.beginObject();
                out.name("id").value(value.getId());
                out.name("email").value(value.getEmail());
                out.name("max_targets").value(value.getMaxTargets());
                out.name("targets")
                        .beginObject()
                        .name("link").value(ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/v1/user/" + value.getId() + "/targets").build().toUriString())
                        .endObject();
                out.name("categories")
                        .beginObject()
                        .name("link").value(ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/v1/user/" + value.getId() + "/categories").build().toUriString())
                        .endObject();
                out.endObject();
            }

            @Override
            public User read(JsonReader in) throws IOException {
                return null;
            }
        };
    }
}
