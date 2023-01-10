/**
 * Copyright (C) 2022 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.summerclouds.common.core.operation;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.summerclouds.common.core.error.ConflictRuntimeException;
import org.summerclouds.common.core.error.ErrorException;
import org.summerclouds.common.core.error.ErrorRuntimeException;
import org.summerclouds.common.core.error.ForbiddenException;
import org.summerclouds.common.core.log.MLog;
import org.summerclouds.common.core.node.INode;
import org.summerclouds.common.core.operation.cmd.CmdOperation;
import org.summerclouds.common.core.tool.MSpring;
import org.summerclouds.common.core.tool.MString;
import org.summerclouds.common.core.tool.MSystem;
import org.summerclouds.common.core.util.Version;

public class OperationManager extends MLog {

    private Map<String, Class<?>> classes;

    @PostConstruct
    protected void setup() {
        classes = new HashMap<>();
        for (Class<? extends Object> clazz :
                MSpring.findAnnotatedClasses(OperationComponent.class, true)) {
            String uri = getOperationUri(clazz);
            log().i("register operation", uri, clazz);
            classes.put(uri, clazz);

            uri = getCommandUri(clazz);
            if (uri != null) {
                log().i("register cmd", uri, clazz);
                classes.put(uri, clazz);
            }
        }
    }

    private String getOperationUri(Class<? extends Object> clazz) {
        OperationComponent def = clazz.getAnnotation(OperationComponent.class);
        String path = def.path();
        if (MString.isEmpty(path)) path = clazz.getCanonicalName();
        String version = def.version();
        if (MString.isEmpty(version)) version = Version.V_0_0_0.toString();
        return "operation://" + path.toLowerCase() + ":" + version.toLowerCase();
    }

    private String getCommandUri(Class<? extends Object> clazz) {
        if (!CmdOperation.class.isAssignableFrom(clazz)) return null;
        OperationComponent def = clazz.getAnnotation(OperationComponent.class);
        String path = def.path();
        if (MString.isEmpty(path)) path = clazz.getSimpleName();
        return "cmd://" + path.toLowerCase();
    }

    public OperationDescription getDescription(String uri) throws Exception {
        return getOperation(uri).getDescription();
    }

    private Operation getOperation(String uri) {
        Class<?> clazz = classes.get(uri);
        if (clazz == null) throw new ConflictRuntimeException("operation {1} not found", uri);
        try {
            return MSystem.createObject(clazz);
        } catch (InstantiationException
                | IllegalAccessException
                | IllegalArgumentException
                | InvocationTargetException
                | NoSuchMethodException
                | SecurityException e) {
            throw new ErrorRuntimeException("initialization of opertion {1} failed", uri, e);
        }
    }

    public OperationResult execute(String uri, INode properties) throws Exception {
        Operation operation = getOperation(uri);
        DefaultTaskContext context = new DefaultTaskContext(operation.getClass());
        context.setParameters(properties);
        if (!operation.hasAccess(context)) throw new ForbiddenException("access denied");
        if (!operation.canExecute(context)) throw new ErrorException("can't execute operation");
        return operation.doExecute(context);
    }

    public String[] getOperations() {
        return classes.keySet().stream()
                .filter(k -> k.startsWith("operation:"))
                .toArray(String[]::new);
    }

    public String[] getCommands() {
        return classes.keySet().stream().filter(k -> k.startsWith("cmd:")).toArray(String[]::new);
    }
}
